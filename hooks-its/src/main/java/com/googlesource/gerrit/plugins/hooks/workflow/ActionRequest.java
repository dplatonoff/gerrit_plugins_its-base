// Copyright (C) 2013 The Android Open Source Project
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.googlesource.gerrit.plugins.hooks.workflow;

import javax.annotation.Nullable;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * An action to take for an {@code ChangeEvent}.
 *
 * Actions are typically related to an Its (e.g.:adding an Its comment, or
 * changing an issue's status).
 */
public class ActionRequest {

  public interface Factory {
    ActionRequest create(String specification);
  }

  @Inject
  public ActionRequest(@Nullable @Assisted String specification) {
  }
}