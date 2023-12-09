package com.example.campusabode

data class Item(val name: String?,
                val email: String?,
                val description: String?,
                val location: String?,
                var imageUrls: List<String>,
                var price: String?,
                var phone: String?,
                var bedrooms: String?,
                var bathrooms: String?,
                var availability: String?,
                var map: String?,
                var youtube: String?
)