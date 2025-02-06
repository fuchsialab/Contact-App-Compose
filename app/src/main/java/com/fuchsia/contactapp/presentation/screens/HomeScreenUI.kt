package com.fuchsia.contactapp.presentation.screens

import android.content.Intent
import android.content.Intent.ACTION_CALL
import android.graphics.BitmapFactory
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.fuchsia.contactapp.data.entities.Contact
import com.fuchsia.contactapp.navigation.routes.Routes
import com.fuchsia.contactapp.presentation.AppState
import com.fuchsia.contactapp.presentation.ContactViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@Composable
fun HomeScreenUI(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ContactViewModel
) {

    val state = viewModel.state.collectAsStateWithLifecycle()

    when (state.value) {
        is AppState.Data -> {

            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(onClick = {
                        navController.navigate(Routes.AddEditScreen())

                    }) {
                        Icon(
                            tint = Color.Blue,
                            imageVector = Icons.Default.Add, contentDescription = "Add Contact"
                        )
                    }
                }

            ) { it ->
                val contacts = (state.value as AppState.Data).data

                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(it),
                    userScrollEnabled = true
                ) {

                    items(contacts) {

                        ContactCard(
                            contact = it,
                            modifier = modifier,
                            navController = navController,
                            viewModel = viewModel
                        )

                    }

                }
            }


        }

        is AppState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()

            }
        }

    }

}

@OptIn(ExperimentalFoundationApi::class, ExperimentalPermissionsApi::class)
@Composable
fun ContactCard(
    contact: Contact,
    navController: NavController,
    modifier: Modifier,
    viewModel: ContactViewModel
) {

    val permissionState = rememberPermissionState(
        android.Manifest.permission.CALL_PHONE
    )

    Card(modifier = modifier
        .fillMaxWidth()
        .padding(8.dp)
        .combinedClickable(
            onClick = {

                if (permissionState.status.isGranted) {
                    val intent = Intent().apply {
                        action = ACTION_CALL
                        data = android.net.Uri.parse("tel:${contact.phoneNumber}")
                    }
                    navController.context.startActivity(intent)

                }else{
                    permissionState.launchPermissionRequest()
                }
            },

        )

    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {

            if (contact.profile == null) {

                val imgName = contact.name.split(" ").filter {
                    it.isNotEmpty()
                }.map {
                    it.first()
                }.joinToString("").uppercase()

                Box(
                    modifier = modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(2.dp, Color.Black, CircleShape)

                ) {
                    Text(
                        text = imgName, modifier = modifier.align(Alignment.Center),
                        color = Color.Blue,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )

                }

//                Image(
//                    painter = painterResource(R.drawable.profile),
//                    contentDescription = null,
//                    modifier = modifier.size(60.dp)
//                )
            } else {

                val image =
                    BitmapFactory.decodeByteArray(contact.profile, 0, contact.profile!!.size)
                Image(
                    bitmap = image.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .border(2.dp, Color.Black, CircleShape)
                )
            }


            Spacer(modifier = Modifier.width(15.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = contact.name, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = contact.phoneNumber)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = contact.email)
            }

            Column(modifier = Modifier, horizontalAlignment = Alignment.End) {
                Icon(
                    tint = Color.Red,
                    imageVector = Icons.Default.Delete,

                    contentDescription = "Delete Contact",
                    modifier = Modifier
                        .size(35.dp)
                        .clickable {
                            viewModel.deleteContact(contact)
                        }
                )

                Spacer(modifier = Modifier.height(10.dp))
                Icon(
                    tint = Color(0xFF1F9DFF),
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Call Contact",
                    modifier = Modifier
                        .size(35.dp)
                        .clickable {
                            navController.navigate(Routes.AddEditScreen(contact.id))

                        }
                )
            }

        }
    }
}
