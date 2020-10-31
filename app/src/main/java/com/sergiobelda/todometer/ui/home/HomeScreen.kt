/*
 * Copyright 2020 Sergio Belda
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sergiobelda.todometer.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AmbientEmphasisLevels
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.ProvideEmphasis
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.TextButton
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.sergiobelda.todometer.R
import com.sergiobelda.todometer.model.TaskState
import com.sergiobelda.todometer.ui.components.ToDometerTitle
import com.sergiobelda.todometer.ui.theme.ToDometerTheme
import com.sergiobelda.todometer.ui.theme.outline
import com.sergiobelda.todometer.ui.utils.ProgressUtil
import com.sergiobelda.todometer.viewmodel.MainViewModel
import java.util.Locale

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun HomeScreen(
    mainViewModel: MainViewModel,
    addTask: () -> Unit,
    addProject: () -> Unit,
    openTask: (Int) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val projectTasksList = mainViewModel.projectList
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetElevation = 16.dp,
        sheetContent = {
            SheetContainer(mainViewModel = mainViewModel, addProject)
        }
    ) {
        Scaffold(
            topBar = {
                ToDometerTopBar()
            },
            bottomBar = {
                if (projectTasksList.isNotEmpty()) {
                    BottomAppBar(
                        backgroundColor = colors.surface,
                        contentColor = contentColorFor(colors.surface),
                        cutoutShape = CircleShape
                    ) {
                        ProvideEmphasis(emphasis = AmbientEmphasisLevels.current.medium) {
                            IconButton(onClick = { sheetState.show() }) {
                                Icon(Icons.Rounded.Menu)
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(onClick = { /* doSomething() */ }) {
                                Icon(Icons.Rounded.MoreVert)
                            }
                        }
                    }
                }
            },
            bodyContent = {
                if (!projectTasksList.isNullOrEmpty()) {
                    ProjectTasksListView(mainViewModel, openTask)
                } else {
                    EmptyProjectTaskListView(addProject)
                }
            },
            floatingActionButton = {
                if (!projectTasksList.isNullOrEmpty()) {
                    FloatingActionButton(
                        icon = { Icon(Icons.Rounded.Add) },
                        onClick = addTask
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            isFloatingActionButtonDocked = true
        )
    }
}

@Composable
fun ToDometerTopBar() {
    Surface(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.height(56.dp).fillMaxWidth()
            ) {
                ToDometerTitle(modifier = Modifier.align(Alignment.Center))
                ProvideEmphasis(emphasis = AmbientEmphasisLevels.current.medium) {
                    IconButton(onClick = {}, modifier = Modifier.align(Alignment.CenterEnd)) {
                        Icon(Icons.Outlined.AccountCircle)
                    }
                }
            }
            Divider(thickness = 1.dp, color = colors.outline)
        }
    }
}

@Composable
fun SheetContainer(mainViewModel: MainViewModel, addProject: () -> Unit) {
    Column(modifier = Modifier.preferredHeight(480.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(56.dp).padding(start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.projects).toUpperCase(Locale.ROOT),
                style = typography.overline
            )
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = addProject) {
                Icon(asset = Icons.Rounded.Add)
                Text(text = stringResource(id = R.string.add_project))
            }
        }
        Divider(thickness = 1.dp, color = colors.outline)
        LazyColumnFor(items = mainViewModel.projectList, modifier = Modifier.preferredHeight(240.dp)) { project ->
            ProvideEmphasis(emphasis = AmbientEmphasisLevels.current.medium) {
                ListItem(
                    modifier = Modifier.clickable(onClick = {}),
                    text = { Text(text = project.name) },
                    icon = { Icon(vectorResource(id = R.drawable.ic_baseline_book_24)) }
                )
            }
        }
        Divider(thickness = 1.dp, color = colors.outline)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(56.dp).padding(start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.tags).toUpperCase(Locale.ROOT),
                style = typography.overline
            )
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = {}) {
                Icon(asset = Icons.Rounded.Add)
                Text(text = stringResource(id = R.string.add_tag))
            }
        }
        Divider(thickness = 1.dp, color = colors.outline)
    }
}

@Composable
fun ProjectTasksListView(
    mainViewModel: MainViewModel,
    openTask: (Int) -> Unit
) {
    val projectTasksList = mainViewModel.projectList
    LazyColumnForIndexed(
        items = projectTasksList,
        modifier = Modifier.padding(32.dp)
    ) { index, project ->
        Text(project.name.toUpperCase(Locale.ROOT), style = typography.overline)
        val progress =
            if (project.tasks.isNotEmpty()) {
                project.tasks.filter { it.state == TaskState.DONE }.size.toFloat() / project.tasks.size.toFloat()
            } else {
                0f
            }
        ProvideEmphasis(emphasis = AmbientEmphasisLevels.current.medium) {
            Text(
                ProgressUtil.getPercentage(progress),
                style = typography.body1,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp).fillMaxWidth()
        )
        project.tasks.forEach { task ->
            TaskItem(
                task,
                updateState = mainViewModel.updateTaskState,
                onClick = openTask
            )
        }
        if (index == projectTasksList.size - 1) {
            Spacer(modifier = Modifier.height(32.dp))
        } else {
            Spacer(modifier = Modifier.height(24.dp))
            Divider(thickness = 1.dp, color = colors.outline)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun EmptyProjectTaskListView(addProject: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                asset = vectorResource(id = R.drawable.project_completed),
                modifier = Modifier.size(240.dp)
            )
            Text(stringResource(id = R.string.you_have_not_any_project))
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp),
                onClick = addProject
            ) {
                Text(stringResource(id = R.string.add_project))
            }
        }
    }
}

@Preview
@Composable
fun EmptyProjectTaskListPreview() {
    ToDometerTheme {
        EmptyProjectTaskListView(addProject = {})
    }
}
