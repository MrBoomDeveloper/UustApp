package com.mrboomdev.uust

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.window.core.layout.WindowSizeClass
import com.mrboomdev.uust.navigation.AppTabs
import com.mrboomdev.uust.navigation.Routes
import com.mrboomdev.uust.utils.add
import com.mrboomdev.uust.utils.iterateIndexed
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import uust.composeapp.generated.resources.*

val LocalBackStack = compositionLocalOf<MutableList<Routes>> { 
    throw NotImplementedError("You didn't provide LocalBackStack!")
}

private fun MutableMap<Int, MutableList<Routes>>.getBackStack(
    index: Int,
    initDefault: Boolean = true,
): MutableList<Routes> {
    return get(index) ?: mutableStateListOf<Routes>().also { backStack ->
        set(index, backStack)
        
        if(initDefault) {
            backStack += AppTabs.entries[index].defaultRoute
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App(initialRoute: Routes) {
    UustTheme {
        val topAppBarBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        val bottomAppBarBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()
        val pagerState = rememberPagerState { AppTabs.entries.size }
        val coroutineScope = rememberCoroutineScope()

        val windowSize = currentWindowAdaptiveInfo().windowSizeClass
        val useRail = windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)

        val backStacks by rememberSerializable<MutableMap<Int, MutableList<Routes>>> {
            mutableStateOf(mutableMapOf())
        }
        
        LaunchedEffect(Unit) {
            backStacks.getBackStack(
                index = 0,
                initDefault = false
            ).apply { 
                if(isEmpty()) {
                    add(initialRoute)
                }
            }
        }
        
        Row(Modifier.fillMaxSize()) {
            if(useRail) {
                Box {
                    NavigationRail(
                        containerColor = MaterialTheme.colorScheme.background,
                        
                        header = {
                            Icon(
                                modifier = Modifier
                                    .padding(vertical = 16.dp)
                                    .size(32.dp),

                                painter = painterResource(Res.drawable.logo),
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = null
                            )
                        }
                    ) {
                        AppTabs.entries.forEachIndexed { index, tab ->
                            NavigationRailItem(
                                selected = pagerState.currentPage == index,
                                
                                label = {
                                    Text(
                                        text = tab.title
                                    )
                                },

                                icon = {
                                    Icon(
                                        modifier = Modifier.size(32.dp),
                                        painter = painterResource(if(pagerState.currentPage == index) {
                                            tab.activeIcon
                                        } else tab.icon),
                                        contentDescription = null,
                                    )
                                },

                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                }
                            )
                        }
                    }

                    VerticalDivider(
                        modifier = Modifier
                            .alpha(.25f)
                            .align(Alignment.CenterEnd)
                    )
                }
            }
            
            Scaffold(
                modifier = Modifier
                    .weight(1f)
                    .nestedScroll(topAppBarBehavior.nestedScrollConnection)
                    .nestedScroll(bottomAppBarBehavior.nestedScrollConnection),

                contentWindowInsets = WindowInsets.safeDrawing.union(WindowInsets.mandatorySystemGestures).let { 
                    if(useRail) {
                        it.only(WindowInsetsSides.Vertical + WindowInsetsSides.End).add(left = 16.dp)
                    } else it
                },

                topBar = {
                    Box {
                        CenterAlignedTopAppBar(
                            scrollBehavior = topAppBarBehavior,
                            
                            windowInsets = WindowInsets.safeDrawing.only(
                                WindowInsetsSides.Horizontal + WindowInsetsSides.Top
                            ).let {
                                if(useRail) {
                                    it.only(WindowInsetsSides.Top + WindowInsetsSides.End).add(left = 16.dp)
                                } else it
                            },

                            colors = TopAppBarDefaults.topAppBarColors(
                                scrolledContainerColor = MaterialTheme.colorScheme.background
                            ),

                            navigationIcon = {
                                val backStack = backStacks.getBackStack(pagerState.currentPage)
                                
                                Crossfade(
                                    modifier = Modifier
                                        .padding(start = 16.dp)
                                        .size(32.dp),
                                    
                                    targetState = backStack.size > 1,
                                    animationSpec = tween(250)
                                ) { canPop ->
                                    if(canPop) {
                                        IconButton(
                                            modifier = Modifier
                                                .scale(1.2f),
                                            
                                            onClick = {
                                                if(backStack.size <= 1) return@IconButton
                                                backStack.removeLastOrNull()
                                            }
                                        ) {
                                            Icon(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(4.dp),
                                                painter = painterResource(Res.drawable.ic_back),
                                                tint = MaterialTheme.colorScheme.primary,
                                                contentDescription = null
                                            )
                                        }
                                        
                                        return@Crossfade
                                    }
                                    
                                    if(useRail) {
                                        return@Crossfade
                                    }

                                    Icon(
                                        modifier = Modifier.fillMaxSize(),
                                        painter = painterResource(Res.drawable.logo),
                                        tint = MaterialTheme.colorScheme.primary,
                                        contentDescription = null
                                    )
                                }
                            },

                            title = {
                                Text(
                                    fontFamily = FontFamily(Font(Res.font.golos_text_bold)),
                                    color = MaterialTheme.colorScheme.primary,
                                    text = "Мы - УУНиТ"
                                )
                            },

                            actions = {
                                IconButton(
                                    onClick = {}
                                ) {
                                    Icon(
                                        modifier = Modifier.size(32.dp),
                                        painter = painterResource(Res.drawable.ic_search),
                                        tint = MaterialTheme.colorScheme.primary,
                                        contentDescription = null
                                    )
                                }
                                
                                IconButton(
                                    onClick = {
                                        backStacks.getBackStack(
                                            pagerState.currentPage
                                        ) += Routes.Settings
                                    }
                                ) {
                                    Icon(
                                        modifier = Modifier.size(32.dp),
                                        painter = painterResource(Res.drawable.ic_account),
                                        tint = MaterialTheme.colorScheme.primary,
                                        contentDescription = null
                                    )
                                }
                            }
                        )

                        HorizontalDivider(
                            modifier = Modifier
                                .alpha(.25f)
                                .align(Alignment.BottomCenter)
                        )
                    }
                },

                bottomBar = {
                    AnimatedVisibility(
                        visible = !useRail && backStacks.getBackStack(
                            index = pagerState.currentPage
                        ).lastOrNull().let { 
                            when(it) {
                                is Routes.Calendar -> false
                                else -> true 
                            } 
                        },
                        
                        enter = fadeIn(spring(stiffness = Spring.StiffnessVeryLow)) + 
                                expandVertically(spring(stiffness = Spring.StiffnessVeryLow)),
                        
                        exit = fadeOut(spring(stiffness = Spring.StiffnessVeryLow)) + 
                                shrinkVertically(spring(stiffness = Spring.StiffnessVeryLow))
                    ) {
                        BottomAppBar(
                            scrollBehavior = bottomAppBarBehavior,
                            containerColor = MaterialTheme.colorScheme.background
                        ) {
                            AppTabs.entries.forEachIndexed { index, tab ->
                                NavigationBarItem(
                                    selected = pagerState.currentPage == index,
                                    
                                    label = {
                                        Text(
                                            text = tab.title
                                        )
                                    },

                                    icon = {
                                        Icon(
                                            modifier = Modifier.size(32.dp),
                                            painter = painterResource(if(pagerState.currentPage == index) {
                                                tab.activeIcon
                                            } else tab.icon),
                                            contentDescription = null,
                                        )
                                    },

                                    onClick = {
                                        if(pagerState.currentPage == index) {
                                            backStacks.getBackStack(pagerState.currentPage).iterateIndexed { index, _ ->
                                                if(index == 0) return@iterateIndexed
                                                remove()
                                            }
                                            
                                            return@NavigationBarItem
                                        }
                                        
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(index)
                                        }
                                    }
                                )
                            }
                        }

                        HorizontalDivider(
                            modifier = Modifier
                                .alpha(.25f)
                        )
                    }
                }
            ) { contentPadding ->
                @Composable
                fun PagerContent(index: Int) {
                    val backStack = backStacks.getBackStack(index)

                    CompositionLocalProvider(
                        LocalBackStack provides backStack
                    ) {
                        NavDisplay(
                            backStack = backStack,
                            onBack = { backStack.removeLastOrNull() },

                            entryDecorators = listOf(
                                rememberSaveableStateHolderNavEntryDecorator(),
                                rememberViewModelStoreNavEntryDecorator()
                            ),

                            transitionSpec = {
                                // Slide in from right when navigating forward
                                slideInHorizontally(initialOffsetX = { it }) togetherWith
                                        slideOutHorizontally(targetOffsetX = { -it })
                            },

                            popTransitionSpec = {
                                // Slide in from left when navigating back
                                slideInHorizontally(initialOffsetX = { -it }) togetherWith
                                        slideOutHorizontally(targetOffsetX = { it })
                            },

                            predictivePopTransitionSpec = {
                                // Slide in from left when navigating back
                                slideInHorizontally(initialOffsetX = { -it }) togetherWith
                                        slideOutHorizontally(targetOffsetX = { it })
                            }
                        ) { route ->
                            NavEntry(route) {
                                route.Content(contentPadding)
                            }
                        }
                    }
                }
                
                if(useRail) {
                    VerticalPager(
                        modifier = Modifier.fillMaxSize(),
                        state = pagerState,
                        userScrollEnabled = false,
                        pageContent = { PagerContent(it) }
                    )
                } else {
                    HorizontalPager(
                        modifier = Modifier.fillMaxSize(),
                        state = pagerState,
                        userScrollEnabled = false,
                        pageContent = { PagerContent(it) }
                    )
                }
            } 
        }
    }
}