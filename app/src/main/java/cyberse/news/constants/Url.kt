package cyberse.news.constants

object Url {
    const val BASE_URL = "https://newsapi.org/v2/"
    const val API_KEY = "302b158345da4d108f95765e8b20b14c"
    const val APPLE_ARTICLES = "${BASE_URL}everything?q=apple&from=2023-12-27&to=2023-12-27&sortBy=popularity&apiKey=${API_KEY}"
    const val TESLA_ARTICLES = "${BASE_URL}everything?q=tesla&from=2023-11-28&sortBy=publishedAt&apiKey=${API_KEY}"
    const val TOP_BUSINESS = "${BASE_URL}top-headlines?country=us&category=business&apiKey=${API_KEY}"
    const val TECH_CRUNCH = "${BASE_URL}top-headlines?sources=techcrunch&apiKey=${API_KEY}"
    const val WSJ_ARTICLES = "${BASE_URL}everything?domains=wsj.com&apiKey=${API_KEY}"
}