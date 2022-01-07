package com.zll.compose.model

import androidx.compose.runtime.Immutable

@Immutable
data class Topic(
    val name: String,
    val courses: Int,
    val imageUrl: String
)

val topics = listOf(
    Topic("Architecture", 58, "https://images.unsplash.com/photo-1479839672679-a46483c0e7c8"),
    Topic("Arts & Crafts", 121, "https://images.unsplash.com/photo-1422246358533-95dcd3d48961"),
    Topic("Business", 78, "https://images.unsplash.com/photo-1507679799987-c73779587ccf"),
    Topic("Culinary", 118, "https://images.unsplash.com/photo-1551218808-94e220e084d2"),
    Topic("Design", 423, "https://images.unsplash.com/photo-1493932484895-752d1471eab5"),
    Topic("Fashion", 92, "https://images.unsplash.com/photo-1517840545241-b491010a8af4"),
    Topic("Film", 165, "https://images.unsplash.com/photo-1518676590629-3dcbd9c5a5c9"),
    Topic("Gaming", 164, "https://images.unsplash.com/photo-1528870884180-5649b20f6435"),
    Topic("Illustration", 326, "https://images.unsplash.com/photo-1526312426976-f4d754fa9bd6"),
    Topic("Lifestyle", 305, "https://images.unsplash.com/photo-1471560090527-d1af5e4e6eb6"),
    Topic("Music", 212, "https://images.unsplash.com/photo-1454922915609-78549ad709bb"),
    Topic("Painting", 172, "https://images.unsplash.com/photo-1461344577544-4e5dc9487184"),
    Topic("Photography", 321, "https://images.unsplash.com/photo-1542567455-cd733f23fbb1"),
    Topic("Technology", 118, "https://images.unsplash.com/photo-1535223289827-42f1e9919769")
)