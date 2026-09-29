/*
 * Cinelex
 * ApiFixtures
 *
 * Created by Esekiel Surbakti on 19/09/26
 */

package co.esekiels.cinelex.network

internal const val MOVIE_RESPONSE: String = """
{
  "page": 1,
  "total_pages": 1,
  "results": [
    {
      "id": 278,
      "title": "The Shawshank Redemption",
      "backdrop_path": "/zfbjgQE1uSd9wiPTX4VzsLi0rGG.jpg",
      "poster_path": "/9cqNxx0GxF0bflZmeSMuL5tnGzr.jpg"
    },
    {
      "id": 238,
      "title": "The Godfather",
      "backdrop_path": "/tmU7GeKVybMWFButWEGl2M4GeiP.jpg",
      "poster_path": "/3bhkrj58Vtu7enYsRolD1fZdja1.jpg"
    }
  ]
}
"""

internal const val MOVIE_DETAILS_RESPONSE: String = """
{
  "id": 278,
  "title": "The Shawshank Redemption",
  "overview": "Two imprisoned men bond over a number of years.",
  "vote_average": 8.7,
  "release_date": "1994-09-23",
  "runtime": 142,
  "genres": [{ "id": 18, "name": "Drama" }],
  "credits": {
    "cast": [{ "id": 504, "name": "Tim Robbins", "character": "Andy Dufresne", "profile_path": null }],
    "crew": [{ "id": 4027, "name": "Frank Darabont", "job": "Director" }]
  },
  "videos": {
    "results": [{ "id": "t1", "key": "abc", "name": "Trailer", "site": "YouTube", "type": "Trailer" }]
  }
}
"""
