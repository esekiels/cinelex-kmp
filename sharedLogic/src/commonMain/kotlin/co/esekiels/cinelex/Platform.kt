package co.esekiels.cinelex

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform