/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.owlylabs.platform.di.android.modules

import com.owlylabs.platform.di.android.scopes.ServiceScoped
import com.owlylabs.platform.services.AudioService
import com.owlylabs.platform.services.BookService
import com.owlylabs.platform.services.ReloadServerDataService
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * This is the debug/release version of ServiceBindingModule.
 */
@Module
abstract class ServiceBindingModule {

    @ServiceScoped
    @ContributesAndroidInjector()
    internal abstract fun reloadServerDataService(): ReloadServerDataService

    @ServiceScoped
    @ContributesAndroidInjector()
    internal abstract fun openBookService(): BookService

    @ServiceScoped
    @ContributesAndroidInjector()
    internal abstract fun openAudioService(): AudioService
}
