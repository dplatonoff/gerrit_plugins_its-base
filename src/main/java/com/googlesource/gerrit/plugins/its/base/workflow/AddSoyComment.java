// Copyright (C) 2017 The Android Open Source Project
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

package com.googlesource.gerrit.plugins.its.base.workflow;

import com.google.common.base.Strings;
import com.google.common.io.CharStreams;
import com.google.inject.Inject;
import com.google.inject.ProvisionException;
import com.google.template.soy.SoyFileSet;
import com.google.template.soy.SoyFileSet.Builder;
import com.google.template.soy.data.SanitizedContent;
import com.google.template.soy.tofu.SoyTofu;
import com.googlesource.gerrit.plugins.its.base.ItsPath;
import com.googlesource.gerrit.plugins.its.base.its.ItsFacade;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adds a short predefined comments to an issue.
 *
 * <p>Comments are added for merging, abandoning, restoring of changes and adding of patch sets.
 */
public class AddSoyComment extends IssueAction {
  private static final Logger log = LoggerFactory.getLogger(AddSoyComment.class);

  public interface Factory {
    AddSoyComment create();
  }

  private final Path templateDir;
  protected HashMap<String, Object> soyContext;

  @Inject
  public AddSoyComment(@ItsPath Path itsPath) {
    this.templateDir = itsPath.resolve("templates");
  }

  private String soyTemplate(
      SoyFileSet.Builder builder,
      String template,
      SanitizedContent.ContentKind kind,
      Map<String, String> properties) {
    Path templatePath = templateDir.resolve(template + ".soy");
    String content;

    try (Reader r = Files.newBufferedReader(templatePath, StandardCharsets.UTF_8)) {
      content = CharStreams.toString(r);
    } catch (IOException err) {
      throw new ProvisionException(
          "Failed to read template file " + templatePath.toAbsolutePath().toString(), err);
    }

    builder.add(content, templatePath.toAbsolutePath().toString());
    SoyTofu.Renderer renderer =
        builder
            .build()
            .compileToTofu()
            .newRenderer("etc.its.templates." + template)
            .setContentKind(kind)
            .setData(properties);
    return renderer.render();
  }

  private String soyTextTemplate(Builder builder, String template, Map<String, String> properties) {
    return soyTemplate(builder, template, SanitizedContent.ContentKind.TEXT, properties);
  }

  @Override
  public void execute(
      ItsFacade its, String issue, ActionRequest actionRequest, Map<String, String> properties)
      throws IOException {
    String comment = buildComment(actionRequest, properties);
    if (!Strings.isNullOrEmpty(comment)) {
      its.addComment(issue, comment);
    }
  }

  private String buildComment(ActionRequest actionRequest, Map<String, String> properties) {
    String template = actionRequest.getParameter(1);
    if (!template.isEmpty()) {
      return soyTextTemplate(SoyFileSet.builder(), template, properties);
    }
    log.error("No template name given in {}", actionRequest);
    return "";
  }
}
