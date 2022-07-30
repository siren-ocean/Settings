/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.setupcompat.logging.internal;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.annotation.VisibleForTesting;
import com.google.android.setupcompat.logging.CustomEvent;
import com.google.android.setupcompat.logging.MetricKey;
import com.google.android.setupcompat.logging.SetupMetricsLogger;

/** Uses to log internal event customization resource list. */
@TargetApi(VERSION_CODES.Q)
public class PartnerCustomizedResourceListMetric {

  public static void logMetrics(Context context, String screenName, Bundle bundle) {
    PersistableBundle logBundle =
        buildLogBundleFromResourceConfigBundle(context.getPackageName(), bundle);
    if (!logBundle.isEmpty()) {
      SetupMetricsLogger.logCustomEvent(
          context,
          CustomEvent.create(MetricKey.get("PartnerCustomizationResource", screenName), logBundle));
    }
  }

  @VisibleForTesting
  public static PersistableBundle buildLogBundleFromResourceConfigBundle(
      String defaultPackageName, Bundle resourceConfigBundle) {
    PersistableBundle persistableBundle = new PersistableBundle();
    for (String key : resourceConfigBundle.keySet()) {
      Bundle resourceExtra = resourceConfigBundle.getBundle(key);
      if (!resourceExtra.getString("packageName", defaultPackageName).equals(defaultPackageName)) {
        persistableBundle.putBoolean(resourceExtra.getString("resourceName", key), true);
      }
    }

    return persistableBundle;
  }
}
