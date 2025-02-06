package com.fuchsia.contactapp.presentation.screens

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fuchsia.contactapp.R
import com.fuchsia.contactapp.data.entities.Contact
import com.fuchsia.contactapp.presentation.AppState
import com.fuchsia.contactapp.presentation.ContactViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

@SuppressLint("Recycle")
@Composable
fun AddEditScreenUI(
    modifier: Modifier = Modifier,
    viewModel: ContactViewModel,
    navController: NavController,
    contactId: Int? = null
) {
    val context = LocalContext.current
    var state = viewModel.state.collectAsStateWithLifecycle()
    var name = remember { mutableStateOf("") }
    var number = remember { mutableStateOf("") }
    var email = remember { mutableStateOf("") }
    var currentContactId = remember { mutableStateOf<Int?>(null) }
    var profile = remember { mutableStateOf<ByteArray?>(null) }
    val pickImage = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            val byteArray = inputStream?.readBytes()
            profile.value = byteArray
            inputStream?.close()

        }
    }


    LaunchedEffect(contactId) {

        if (contactId != null) {
            val contacts = (state.value as AppState.Data).data.find {
                it.id == contactId
            }
            name.value = contacts?.name ?: ""
            number.value = contacts?.phoneNumber ?: ""
            email.value = contacts?.email ?: ""
            currentContactId.value = contacts?.id
            profile.value = contacts?.profile
        }
    }


    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(100.dp))

        if (profile.value == null) {

            Image(
                painter = painterResource(R.drawable.profile),
                contentDescription = null,
                modifier = modifier
                    .size(100.dp)
                    .border(2.dp, Color.Black, CircleShape)
                    .clickable {
                        pickImage.launch("image/*")

                    }
            )
        } else {

            val image = BitmapFactory.decodeByteArray(profile.value, 0, profile.value!!.size)
            Image(
                bitmap = image.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Black, CircleShape)
                    .clickable {
                        pickImage.launch("image/*")
                    }

            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = name.value, onValueChange = {
            name.value = it

        }, label = { Text("Enter Name") })

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = number.value, onValueChange = {
            number.value = it

        }, label = { Text("Enter Number") })
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = email.value, onValueChange = {
            email.value = it

        }, label = { Text("Enter Email") })
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = modifier.width(250.dp),
            onClick = {
            viewModel.upsertContact(
                Contact(
                    name = name.value,
                    phoneNumber = number.value,
                    email = email.value,
                    dateOfEdit = System.currentTimeMillis(),
                    id = currentContactId.value ?: 0,
                    profile = profile.value
                )
            )
            navController.navigateUp()
        }) {
            Text(text = "Save Contact")

        }

    }

}




