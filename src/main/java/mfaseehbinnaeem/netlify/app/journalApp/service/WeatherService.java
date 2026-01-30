package mfaseehbinnaeem.netlify.app.journalApp.service;

import mfaseehbinnaeem.netlify.app.journalApp.api.response.WeatherResponse;
import mfaseehbinnaeem.netlify.app.journalApp.cache.AppCache;
import mfaseehbinnaeem.netlify.app.journalApp.constants.Placeholders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    @Value("${weather.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppCache appCache;

    @Autowired
    private RedisService redisService;

    public WeatherResponse getWeather(String city) {
        WeatherResponse weatherResponse = redisService.get("weather_of_" + city, WeatherResponse.class);

        if (weatherResponse != null) {
            return weatherResponse;
        } else {
            String api = appCache.appCache.get(AppCache.keys.WEATHER_API.toString()).replace(Placeholders.API_KEY, apiKey).replace(Placeholders.CITY, city);

            ResponseEntity<WeatherResponse> response = restTemplate.exchange(api, HttpMethod.GET, null, WeatherResponse.class);
            WeatherResponse weatherResponseBody = response.getBody();

            if (response != null) {
                redisService.set("weather_of_" + city, weatherResponseBody, 300l);
            }

            return weatherResponseBody;
        }
    }
}
