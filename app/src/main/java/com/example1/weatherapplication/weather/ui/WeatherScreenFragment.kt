package com.example1.weatherapplication.weather.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Thin
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.compose.rememberAsyncImagePainter
import com.example1.weatherapplication.R
import com.example1.weatherapplication.core.utils.DateTimeUtils.fixApiUrl
import com.example1.weatherapplication.core.utils.DateTimeUtils.formatDateFromString
import com.example1.weatherapplication.core.utils.DateTimeUtils.getDayOfWeek
import com.example1.weatherapplication.core.utils.Grey400
import com.example1.weatherapplication.core.utils.Grey50
import com.example1.weatherapplication.core.utils.Grey500
import com.example1.weatherapplication.core.utils.LottieAnimationView
import com.example1.weatherapplication.core.utils.Poppins
import com.example1.weatherapplication.core.utils.WeatherGray
import com.example1.weatherapplication.core.utils.isNetworkAvailable
import com.example1.weatherapplication.core.utils.setComposeContent
import com.example1.weatherapplication.weather.domain.ForecastDay
import com.example1.weatherapplication.weather.domain.Hour
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherScreenFragment : Fragment() {

    private val viewModule by viewModel<WeatherScreenViewModule>()
    private var isNetworkAvailable: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return setComposeContent {
            WeatherScreen(modifier = Modifier, state = viewModule.state)
        }
    }

    @SuppressLint("UseOfNonLambdaOffsetOverload")
    @Composable
    fun WeatherScreen(modifier: Modifier = Modifier, state: WeatherState) {
        val scrollState = rememberScrollState()

        var isVisible by remember { mutableStateOf(false) }
        var offsetY by remember { mutableStateOf(0f) } // To track the drag offset
        val dragState = rememberDraggableState { delta ->
            offsetY = (offsetY + delta).coerceIn(0f, 300f) // Limit drag range
        }

        val isBottomBarVisible by remember {
            derivedStateOf {
                !scrollState.isScrollInProgress
            }
        }

        AndroidView(factory = { context ->
            val view = View(context)
            view.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                            View.SYSTEM_UI_FLAG_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    )
            view
        })
        if (!isNetworkAvailable) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.no_internet_connection),
                    style = TextStyle(
                        fontFamily = Poppins,
                        fontWeight = W500,
                        fontSize = 20.sp,
                        color = WeatherGray
                    ),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .draggable(
                        orientation = Orientation.Vertical,
                        state = dragState,
                        onDragStopped = {
                            if (offsetY > 150f) {
                                isVisible = false
                            }
                            offsetY = 0f
                        }
                    )
                    .offset(y = offsetY.dp) // Apply drag offset
            ) {

                if (state.isLoading) {
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF3389F1),
                                        Color(0xFFFFFCF6)
                                    ),
                                    start = Offset(0f, 1000f),
                                    end = Offset(0f, 1800f)
                                )
                            )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            LottieAnimationView(
                                resId = R.raw.loading,
                                modifier = Modifier.size(200.dp)
                            )
                        }
                    }
                }
                if (!state.isLoading) {
                    LaunchedEffect(Unit) {
                        isVisible = true
                    }
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF3389F1),
                                        Color(0xFFFFFCF6)
                                    ),
                                    start = Offset(0f, 1200f),
                                    end = Offset(0f, 1900f)
                                )
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp)
                                .padding(top = 40.dp, bottom = 70.dp),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "${formatDateFromString(state.forecast?.forecastday?.firstOrNull()?.date ?: "")}, ${
                                    getDayOfWeek(
                                        state.forecast?.forecastday?.firstOrNull()?.date ?: ""
                                    )
                                }",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = W400,
                                    fontFamily = Poppins,
                                    color = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = state.location?.name ?: "",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = W500,
                                    fontFamily = Poppins,
                                    color = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = state.current?.condition?.text ?: "",
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        fontWeight = W500,
                                        fontFamily = Poppins,
                                        color = Color.White
                                    ),
                                    textAlign = TextAlign.Center
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentSize(), contentAlignment = Alignment.Center
                            ) {
                                when (state.current?.condition?.code) {
                                    1000 -> {
                                        if(state.current.isDay == 1){
                                            LottieAnimationView(
                                                modifier = Modifier.size(250.dp),
                                                resId = R.raw.sunny
                                            )
                                        } else {
                                            LottieAnimationView(
                                                modifier = Modifier.size(250.dp),
                                                resId = R.raw.moon
                                            )
                                        }

                                    }

                                    1003 -> {
                                        if (state.current.isDay == 1) {
                                            LottieAnimationView(
                                                modifier = Modifier.size(250.dp),
                                                resId = R.raw.partly_cloudy
                                            )
                                        } else {
                                            LottieAnimationView(
                                                resId = R.raw.partly_cloudy_night,
                                                modifier = Modifier.size(250.dp)
                                            )
                                        }
                                    }

                                    1006, 1009 -> {
                                        LottieAnimationView(
                                            modifier = Modifier.size(250.dp),
                                            resId = R.raw.cloudy
                                        )
                                    }

                                    1063, 1072, 1150, 1153, 1168, 1171, 1180, 1183, 1186, 1189, 1198, 1201, 1240, 1243  -> {
                                        LottieAnimationView(
                                            modifier = Modifier.size(250.dp),
                                            resId = R.raw.patchy_rain
                                        )
                                    }

                                    1087 -> {
                                        LottieAnimationView(
                                            modifier = Modifier.size(250.dp),
                                            resId = R.raw.thunder
                                        )
                                    }

                                    1192, 1195, 1273, 1276 -> {
                                        LottieAnimationView(
                                            modifier = Modifier.size(250.dp),
                                            resId = R.raw.rain_with_thunder
                                        )
                                    }
                                }
                            }


                            Row(
                                modifier = Modifier.padding(end = 20.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = state.current?.tempC.toString() + "°C",
                                    style = TextStyle(
                                        fontSize = 60.sp,
                                        fontWeight = W700,
                                        fontFamily = Poppins,
                                        color = Color.White
                                    )
                                )
                                Spacer(modifier = Modifier.width(30.dp))
                                Column {
                                    Text(
                                        text = "feels like",
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontWeight = W400,
                                            fontFamily = Poppins,
                                            color = Color.White
                                        ),
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                    Text(
                                        text = "${state.current?.feelslikeC ?: 0}°C",
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontWeight = W700,
                                            fontFamily = Poppins,
                                            color = Color.White
                                        )
                                    )
                                }

                            }
                            Spacer(modifier = Modifier.height(30.dp))
                            Row(modifier = Modifier) {
                                Row(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = stringResource(id = R.string.humidity),
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontWeight = W400,
                                            fontFamily = Poppins,
                                            color = Color.White
                                        )
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        text = "${state.current?.humidity}%",
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontWeight = W700,
                                            fontFamily = Poppins,
                                            color = Color.White
                                        )
                                    )
                                }
                                VerticalDivider(
                                    color = Color.White,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .padding(start = 14.dp)
                                )
                                Row(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = stringResource(id = R.string.uv_index),
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontWeight = W400,
                                            fontFamily = Poppins,
                                            color = Color.White
                                        )
                                    )
                                    Spacer(modifier = Modifier.weight(1f))

                                    Text(
                                        text = "${state.current?.uv}/10",
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontWeight = W700,
                                            fontFamily = Poppins,
                                            color = Color.White
                                        )
                                    )
                                }
                            }
                            HorizontalDivider(color = Color.White)
                            Row(modifier = Modifier) {
                                Row(modifier = Modifier.weight(1f)) {
                                    Text(
                                        modifier = Modifier.padding(top = 10.dp),
                                        text = stringResource(id = R.string.chance_of_rain),
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontWeight = W400,
                                            fontFamily = Poppins,
                                            color = Color.White
                                        )
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        modifier = Modifier.padding(top = 10.dp),
                                        text = "${state.forecast?.forecastday?.firstOrNull()?.day?.dailyChanceOfRain}%",
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontWeight = W700,
                                            fontFamily = Poppins,
                                            color = Color.White
                                        )
                                    )
                                }
                                VerticalDivider(
                                    color = Color.White,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .padding(start = 14.dp)
                                )
                                Row(modifier = Modifier.weight(1f)) {
                                    Text(
                                        modifier = Modifier.padding(top = 10.dp),
                                        text = stringResource(id = R.string.wind),
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontWeight = W400,
                                            fontFamily = Poppins,
                                            color = Color.White
                                        )
                                    )
                                    Spacer(modifier = Modifier.weight(1f))

                                    Text(
                                        modifier = Modifier.padding(top = 10.dp),
                                        text = "${state.current?.windKph} km/h",
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontWeight = W700,
                                            fontFamily = Poppins,
                                            color = Color.White
                                        ),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(30.dp))
                            AnimatedVisibility(
                                visible = isVisible,
                                enter = slideInHorizontally(
                                    initialOffsetX = { fullWidth -> fullWidth }
                                ) + fadeIn(animationSpec = tween(durationMillis = 2000)),
                                exit = slideOutHorizontally(
                                    targetOffsetX = { fullWidth -> fullWidth }
                                ) + fadeOut(animationSpec = tween(durationMillis = 2000))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState())
                                        .background(
                                            color = Color.White,
                                            shape = RoundedCornerShape(40.dp)
                                        )
                                )
                                {
                                    Row(modifier = Modifier) {
                                        state.forecast?.forecastday?.firstOrNull()?.hour?.let { hours ->
                                            hours.forEach { hour ->
                                                WeatherCard(hour = hour, modifier = Modifier)
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            AnimatedVisibility(
                                visible = isVisible,
                                enter = slideInVertically(
                                    initialOffsetY = { fullHeight -> fullHeight },
                                    animationSpec = tween(durationMillis = 1000)
                                )
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(
                                            border = BorderStroke(
                                                1.dp,
                                                color = Color.LightGray
                                            ),
                                            shape = RoundedCornerShape(20.dp)
                                        )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        verticalAlignment = Alignment.Top,
                                        horizontalArrangement = Arrangement.Start
                                    )
                                    {
                                        Column(
                                            modifier = Modifier
                                                .padding(start = 20.dp)
                                                .padding(vertical = 20.dp)
                                        ) {
                                            Text(
                                                text = stringResource(id = R.string.tomorrow),
                                                style = TextStyle(
                                                    fontSize = 16.sp,
                                                    fontWeight = W700,
                                                    fontFamily = Poppins,
                                                    color = WeatherGray
                                                )
                                            )
                                            Text(
                                                text = "${
                                                    formatDateFromString(
                                                        state.forecast?.forecastday?.get(
                                                            1
                                                        )?.date ?: ""
                                                    )
                                                },\n${
                                                    getDayOfWeek(
                                                        state.forecast?.forecastday?.get(
                                                            1
                                                        )?.date ?: ""
                                                    )
                                                }",
                                                style = TextStyle(
                                                    fontSize = 14.sp,
                                                    fontWeight = W500,
                                                    fontFamily = Poppins,
                                                    color = Grey400
                                                )
                                            )
                                        }

                                        Box(
                                            modifier = Modifier
                                                .wrapContentSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            when (state.forecast?.forecastday?.get(1)?.day?.condition?.code) {
                                                1000 -> {
                                                    LottieAnimationView(
                                                        modifier = Modifier.size(80.dp),
                                                        resId = R.raw.sunny
                                                    )
                                                }

                                                1003 -> {
                                                    LottieAnimationView(
                                                        modifier = Modifier.size(80.dp),
                                                        resId = R.raw.partly_cloudy
                                                    )
                                                }

                                                1006, 1009 -> {
                                                    LottieAnimationView(
                                                        modifier = Modifier.size(80.dp),
                                                        resId = R.raw.cloudy
                                                    )
                                                }

                                                1063, 1072, 1150, 1153, 1168, 1171, 1180, 1183, 1186, 1189, 1198, 1201, 1240, 1243  -> {
                                                    LottieAnimationView(
                                                        modifier = Modifier.size(80.dp),
                                                        resId = R.raw.patchy_rain
                                                    )
                                                }

                                                1087 -> {
                                                    LottieAnimationView(
                                                        modifier = Modifier.size(80.dp),
                                                        resId = R.raw.thunder
                                                    )
                                                }

                                                1192, 1195, 1273, 1276 -> {
                                                    LottieAnimationView(
                                                        modifier = Modifier.size(80.dp),
                                                        resId = R.raw.rain_with_thunder
                                                    )
                                                }
                                            }
                                        }
                                        Column(
                                            modifier = Modifier
                                                .padding(vertical = 20.dp)
                                                .padding(start = 15.dp)
                                        )
                                        {
                                            Text(
                                                text = "${state.forecast?.forecastday?.get(1)?.day?.avgtempC}°C",
                                                style = TextStyle(
                                                    fontFamily = Poppins,
                                                    fontSize = 16.sp,
                                                    fontWeight = W700,
                                                    color = WeatherGray
                                                )
                                            )
                                            Text(
                                                text = "${state.forecast?.forecastday?.get(1)?.day?.condition?.text}",
                                                style = TextStyle(
                                                    fontWeight = W500,
                                                    fontSize = 14.sp,
                                                    fontFamily = Poppins,
                                                    color = Color.LightGray
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Row(modifier = Modifier) {
                                Row(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = stringResource(id = R.string.humidity),
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontWeight = W400,
                                            fontFamily = Poppins,
                                            color = Grey500
                                        )
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        text = "${state.forecast?.forecastday?.get(1)?.day?.avghumidity}%",
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontWeight = W700,
                                            fontFamily = Poppins,
                                            color = WeatherGray
                                        )
                                    )
                                }
                                VerticalDivider(
                                    color = Color.LightGray,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .padding(start = 14.dp)
                                )
                                Row(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = stringResource(id = R.string.uv_index),
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontWeight = W400,
                                            fontFamily = Poppins,
                                            color = Grey500
                                        )
                                    )
                                    Spacer(modifier = Modifier.weight(1f))

                                    Text(
                                        text = "${state.forecast?.forecastday?.get(1)?.day?.uv}/10",
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontWeight = W700,
                                            fontFamily = Poppins,
                                            color = WeatherGray
                                        )
                                    )
                                }
                            }
                            HorizontalDivider(color = Color.LightGray)
                            Row(modifier = Modifier) {
                                Row(modifier = Modifier.weight(1f)) {
                                    Text(
                                        modifier = Modifier.padding(top = 10.dp),
                                        text = stringResource(id = R.string.chance_of_rain),
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontWeight = W400,
                                            fontFamily = Poppins,
                                            color = Grey500
                                        )
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        modifier = Modifier.padding(top = 10.dp),
                                        text = "${state.forecast?.forecastday?.get(1)?.day?.dailyChanceOfRain}%",
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontWeight = W700,
                                            fontFamily = Poppins,
                                            color = WeatherGray
                                        )
                                    )
                                }
                                VerticalDivider(
                                    color = Color.LightGray,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .padding(start = 14.dp)
                                )
                                Row(modifier = Modifier.weight(1f)) {
                                    Text(
                                        modifier = Modifier.padding(top = 10.dp),
                                        text = stringResource(id = R.string.wind),
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontWeight = W400,
                                            fontFamily = Poppins,
                                            color = Grey500
                                        )
                                    )
                                    Spacer(modifier = Modifier.weight(1f))

                                    Text(
                                        modifier = Modifier.padding(top = 10.dp),
                                        text = "${state.forecast?.forecastday?.get(1)?.day?.maxwindKph} km/h",
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontWeight = W700,
                                            fontFamily = Poppins,
                                            color = WeatherGray
                                        ),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(30.dp))


                            if (state.forecast?.forecastday?.size == 10) {
                                Text(
                                    text = stringResource(id = R.string.days_forecast),
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        fontWeight = W700,
                                        fontFamily = Poppins,
                                        color = WeatherGray
                                    )
                                )

                                Spacer(modifier = Modifier.height(10.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .size(500.dp)
                                        .background(Color.White, shape = RoundedCornerShape(26.dp))
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                    ) {
                                        state.forecast.forecastday.drop(2).forEach { forecastDay ->
                                            ForecastItem(
                                                forecastDay = forecastDay,
                                                date = forecastDay.date ?: ""
                                            )
                                        }
                                    }
                                }
                            }


                        }
                    }
                }



                AnimatedVisibility(
                    visible = isBottomBarVisible,
                    enter = slideInVertically(
                        initialOffsetY = { fullHeight -> fullHeight },
                        animationSpec = tween(durationMillis = 1000)
                    ),
                    exit = slideOutVertically(
                        targetOffsetY = { fullHeight -> fullHeight }, // Slide out downwards
                        animationSpec = tween(durationMillis = 1000)
                    ),
                    modifier = Modifier.align(Alignment.BottomCenter)

                ) {
                    BottomAppBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .wrapContentHeight(),
                        containerColor = Color.White,
                        tonalElevation = 100.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, bottom = 20.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Box(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .background(
                                        color = Color.LightGray,
                                        shape = RoundedCornerShape(70.dp)
                                    )
                            ) {
                                IconButton(onClick = {
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.compass),
                                        contentDescription = "Compass"
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .background(
                                        color = Color.White,
                                        shape = RoundedCornerShape(70.dp)
                                    )
                            ) {
                                IconButton(onClick = {
                                    findNavController().navigate(R.id.action_weatherScreenFragment_to_UVScreenFragment2)
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.sun),
                                        contentDescription = "Sun"
                                    )
                                }
                            }
                        }
                    }
                }

            }
        }

    }

    @Composable
    fun WeatherCard(
        modifier: Modifier = Modifier,
        hour: Hour
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Row(modifier = Modifier) {
                Box(
                    modifier = Modifier.size(40.dp), contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier.size(40.dp),
                        painter = rememberAsyncImagePainter(
                            model = fixApiUrl(
                                hour.condition?.icon ?: ""
                            )
                        ),
                        contentDescription = ""
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier) {
                    Text(
                        text = "${hour.tempC}°C",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = W500,
                            fontFamily = Poppins,
                            color = WeatherGray
                        )
                    )
                    Text(
                        text = extractTime(hour.time ?: ""),
                        style = TextStyle(
                            fontFamily = Poppins,
                            fontSize = 12.sp,
                            fontWeight = Thin,
                            color = Color.Gray
                        )
                    )
                }
            }
        }
    }

    @Composable
    fun ForecastItem(modifier: Modifier = Modifier, forecastDay: ForecastDay, date: String) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
                .background(Color.White, shape = RoundedCornerShape(20.dp))
        )
        {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {
                Text(
                    modifier = Modifier.weight(2f),
                    text = formatDateFromString(date),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = W700,
                        fontFamily = Poppins,
                        color = WeatherGray
                    )
                )
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .weight(1f)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = fixApiUrl(
                                forecastDay.day?.condition?.icon ?: ""
                            )
                        ),
                        modifier = Modifier.size(50.dp),
                        contentDescription = ""
                    )
                }
                Text(
                    modifier = Modifier.weight(1f),
                    text = "${forecastDay.day?.avgtempC}°C",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = Poppins,
                        fontWeight = W600,
                        color = WeatherGray
                    )
                )
                Text(
                    modifier = Modifier.weight(2f),
                    text = forecastDay.day?.condition?.text ?: "",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = W500,
                        fontFamily = Poppins,
                        color = WeatherGray
                    ),
                    textAlign = TextAlign.End
                )

            }
            HorizontalDivider(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .align(Alignment.BottomCenter),
                thickness = 1.dp,
                color = Grey50
            )
        }
    }

    fun extractTime(dateString: String): String {
        return dateString.substring(11, 16)
    }

}