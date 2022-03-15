package com.example.moviesapp.ui.screen.movie_list_screen.cell

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.moviesapp.R

@Preview
@Composable
fun MovieAdvertiseCell() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Gray)
    ) {
        Text(
            text = stringResource(R.string.advertisment),
            style = MaterialTheme.typography.h4,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(10.dp)
        )
    }
}