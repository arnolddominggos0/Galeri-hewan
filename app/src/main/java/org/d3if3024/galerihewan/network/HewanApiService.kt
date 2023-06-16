    package org.d3if3024.galerihewan.network

    import retrofit2.Retrofit
    import retrofit2.converter.scalars.ScalarsConverterFactory
    import retrofit2.http.GET

    private const val BASE_URL = "https://raw.githubusercontent.com/" + "danirahmad/JSON_Hewan/master/Hewan_Karnivora/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    interface HewanApiService{
        @GET("DataHewan.json")
        suspend fun getHewan(): String
    }

    object HewanApi {
        val service: HewanApiService by lazy {
            retrofit.create(HewanApiService::class.java)
        }
    }