package com.example1.weatherapplication.core.utils

import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

fun Fragment.setComposeContent(
    strategy: ViewCompositionStrategy = ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed,
    content: @Composable () -> Unit
): View {
    return ComposeView(requireContext()).apply {
        setViewCompositionStrategy(strategy)
        setContent(content)
    }
}

fun BottomSheetDialogFragment.setComposeContent(
    strategy: ViewCompositionStrategy = ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed,
    content: @Composable () -> Unit
): View {
    return ComposeView(requireContext()).apply {
        setViewCompositionStrategy(strategy)
        setContent(content)
    }
}