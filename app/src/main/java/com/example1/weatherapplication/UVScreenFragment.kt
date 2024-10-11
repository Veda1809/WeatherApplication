package com.example1.weatherapplication

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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example1.weatherapplication.core.utils.DateTimeUtils.calculateDaylightHours
import com.example1.weatherapplication.core.utils.DateTimeUtils.formatDateFromString
import com.example1.weatherapplication.core.utils.DateTimeUtils.getDayOfWeek
import com.example1.weatherapplication.core.utils.Grey500
import com.example1.weatherapplication.core.utils.LottieAnimationView
import com.example1.weatherapplication.core.utils.Poppins
import com.example1.weatherapplication.core.utils.WeatherGray
import com.example1.weatherapplication.core.utils.setComposeContent
import com.example1.weatherapplication.weather.ui.WeatherScreenViewModule
import com.example1.weatherapplication.weather.ui.WeatherState
import org.koin.androidx.viewmodel.ext.android.viewModel

class UVScreenFragment : Fragment() {

    private val viewModule by viewModel<WeatherScreenViewModule>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return setComposeContent {
            UVScreen(modifier = Modifier, state = viewModule.state)
        }
    }

    @Composable
    fun UVScreen(modifier: Modifier = Modifier, state: WeatherState) {
        val scrollState = rememberScrollState()
        var selectedIcon by remember { mutableStateOf(true) }

        var isVisible by remember { mutableStateOf(false) }

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
        Box(
            modifier = modifier
                .fillMaxSize()
        ) {

            if (state.isLoading) {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFFF753E),
                                    Color(0xFFFFFCF6)
                                ),
                                start = Offset(500f, 200f),
                                end = Offset(0f, 1500f)
                            )
                        )
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        LottieAnimationView(resId = R.raw.loading, modifier = Modifier.size(200.dp))
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
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFFF753E),
                                    Color(0xFFFFFCF6)
                                ),
                                start = Offset(500f, 200f),
                                end = Offset(0f, 1500f)
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
                            ),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = state.location?.name ?: "",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = W500,
                                fontFamily = Poppins,
                                color = Color.White
                            ),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        val uvText = when (state.current?.uv!!) {
                            in 1.0..4.0 -> stringResource(id = R.string.low_uv)
                            in 4.0..7.0 -> stringResource(id = R.string.moderate_uv)
                            in 7.0..10.0 -> stringResource(id = R.string.high_uv)
                            else -> ""
                        }
                        Box(
                            modifier = Modifier
                                .wrapContentSize()
                                .align(Alignment.End)
                                .padding(end = 0.dp)
                        ) {
                            Text(
                                text = uvText,
                                style = TextStyle(
                                    fontSize = 40.sp,
                                    fontFamily = Poppins,
                                    fontWeight = W700,
                                    color = Color.White,
                                    lineHeight = 40.sp // Matching fontSize and lineHeight
                                ),
                                modifier = Modifier
                                    .graphicsLayer {
                                        rotationZ = 90f
                                        translationX =
                                            if (uvText == getString(R.string.moderate_uv)) 330f else if (uvText == getString(
                                                    R.string.low_uv
                                                )
                                            ) 160f else 180f
                                        translationY =
                                            if (uvText == getString(R.string.moderate_uv)) 85f else if (uvText == getString(
                                                    R.string.low_uv
                                                )
                                            ) -25f else -15f
                                    }
                            )
                        }
                        Spacer(modifier = Modifier.height(200.dp))

                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontSize = 60.sp,
                                        fontFamily = Poppins,
                                        fontWeight = W500,
                                        color = WeatherGray
                                    )
                                ) {
                                    append("${state.current?.uv}/10")
                                }

                                append("   ")

                                withStyle(
                                    style = SpanStyle(
                                        fontSize = 16.sp,
                                        fontFamily = Poppins,
                                        fontWeight = W400,
                                        color = WeatherGray
                                    )
                                ) {
                                    append("UV Index")
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        AnimatedVisibility(
                            visible = isVisible,
                            enter = slideInHorizontally(
                                initialOffsetX = { fullWidth -> fullWidth }
                            ) + fadeIn(animationSpec = tween(durationMillis = 3000)),
                            exit = slideOutHorizontally(
                                targetOffsetX = { fullWidth -> fullWidth }
                            ) + fadeOut(animationSpec = tween(durationMillis = 3000))
                        ) {
                            Box(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .background(
                                        color = Color.White,
                                        shape = RoundedCornerShape(20.dp)
                                    )
                            ) {
                                Column(
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp,
                                        vertical = 16.dp
                                    )
                                ) {
                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        Row(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = stringResource(id = R.string.sunrise),
                                                style = TextStyle(
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.W400,
                                                    fontFamily = Poppins,
                                                    color = Grey500
                                                ),
                                                textAlign = TextAlign.Center
                                            )
                                            Spacer(modifier = Modifier.weight(1f))
                                            Text(
                                                text = "${state.forecast?.forecastday?.firstOrNull()?.astro?.sunrise}",
                                                style = TextStyle(
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.W700,
                                                    fontFamily = Poppins,
                                                    color = WeatherGray
                                                ),
                                                textAlign = TextAlign.Center
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
                                                text = stringResource(id = R.string.sunset),
                                                style = TextStyle(
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.W400,
                                                    fontFamily = Poppins,
                                                    color = Grey500
                                                ),
                                                textAlign = TextAlign.Center
                                            )
                                            Spacer(modifier = Modifier.weight(1f))
                                            Text(
                                                text = "${state.forecast?.forecastday?.firstOrNull()?.astro?.sunset}",
                                                style = TextStyle(
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.W700,
                                                    fontFamily = Poppins,
                                                    color = WeatherGray
                                                ),
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                    HorizontalDivider(
                                        color = Color.LightGray
                                    )
                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        Row(modifier = Modifier.weight(1f)) {
                                            Text(
                                                modifier = Modifier.padding(top = 10.dp),
                                                text = stringResource(id = R.string.moonrise),
                                                style = TextStyle(
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.W400,
                                                    fontFamily = Poppins,
                                                    color = Grey500
                                                ),
                                                textAlign = TextAlign.Center
                                            )
                                            Spacer(modifier = Modifier.weight(1f))
                                            Text(
                                                modifier = Modifier.padding(vertical = 10.dp),
                                                text = "${state.forecast?.forecastday?.firstOrNull()?.astro?.moonrise}",
                                                style = TextStyle(
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.W700,
                                                    fontFamily = Poppins,
                                                    color = WeatherGray
                                                ),
                                                textAlign = TextAlign.Center
                                            )
                                        }

                                        VerticalDivider(
                                            color = Color.LightGray,
                                            modifier = Modifier
                                                .size(22.dp, 40.dp)
                                                .padding(start = 10.dp)
                                        )

                                        Row(modifier = Modifier.weight(1f)) {
                                            Text(
                                                modifier = Modifier.padding(top = 10.dp),
                                                text = stringResource(id = R.string.moonset),
                                                style = TextStyle(
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.W400,
                                                    fontFamily = Poppins,
                                                    color = Grey500
                                                ),
                                                textAlign = TextAlign.Center
                                            )
                                            Spacer(modifier = Modifier.weight(1f))
                                            Text(
                                                modifier = Modifier.padding(top = 10.dp),
                                                text = "${state.forecast?.forecastday?.firstOrNull()?.astro?.moonset}",
                                                style = TextStyle(
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.W700,
                                                    fontFamily = Poppins,
                                                    color = WeatherGray
                                                ),
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }

                                    HorizontalDivider(
                                        color = Color.LightGray
                                    )

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 10.dp)
                                    ) {
                                        Text(
                                            text = stringResource(id = R.string.daylight_hours),
                                            style = TextStyle(
                                                fontSize = 14.sp,
                                                fontWeight = W400,
                                                fontFamily = Poppins,
                                                color = Grey500
                                            )
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(
                                            text = calculateDaylightHours(
                                                state.forecast?.forecastday?.firstOrNull()?.astro?.sunrise
                                                    ?: "",
                                                state.forecast?.forecastday?.firstOrNull()?.astro?.sunset
                                                    ?: ""
                                            ),
                                            style = TextStyle(
                                                fontSize = 14.sp,
                                                fontFamily = Poppins,
                                                fontWeight = W700,
                                                color = WeatherGray
                                            )
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
                                .background(color = Color.White, shape = RoundedCornerShape(70.dp))
                        ) {
                            IconButton(onClick = {
                                findNavController().navigate(R.id.action_UVScreenFragment_to_weatherScreenFragment2)
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
                                    color = Color.LightGray,
                                    shape = RoundedCornerShape(70.dp)
                                )
                        ) {
                            IconButton(modifier = Modifier, onClick = {
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