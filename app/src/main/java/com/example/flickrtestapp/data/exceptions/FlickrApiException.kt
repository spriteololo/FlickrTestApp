package com.example.flickrtestapp.data.exceptions

import com.example.flickrtestapp.data.apimodel.ErrorResponse
import okio.IOException

class FlickrApiException(error: ErrorResponse): IOException() {
    override val message = error.message
}