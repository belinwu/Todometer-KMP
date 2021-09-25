/*
 * Copyright 2021 Sergio Belda
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

package com.sergiobelda.todometer.common.repository

import com.sergiobelda.todometer.common.data.Result
import com.sergiobelda.todometer.common.model.Project
import kotlinx.coroutines.flow.Flow

interface IProjectRepository {

    fun getProject(id: String): Flow<Result<Project>>

    fun getProjects(): Flow<Result<List<Project>>>

    suspend fun refreshProject(id: String)

    suspend fun insertProject(name: String): Result<String>

    suspend fun updateProject(project: Project)

    suspend fun refreshProjects()

    suspend fun deleteProject(id: String)
    // suspend fun deleteProject(id: String): Result<String>
}
