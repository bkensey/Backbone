/*
 * Copyright (C) BrandroidTools
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.toolify.backbone.bus.events;

public class FilesystemStatusUpdateEvent extends BusEvent {
	public final int status;

    public static final int INDICATOR_LOCKED = 0;
    public static final int INDICATOR_UNLOCKED = 1;
    public static final int INDICATOR_WARNING = 2;
    public static final int INDICATOR_REFRESHING = 3;
    public static final int INDICATOR_STOP_REFRESHING = 4;

	public FilesystemStatusUpdateEvent(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return new StringBuilder("(")
			.append("bus event: filesystem info status update")
            .append(" - status code: ")
			.append(Integer.toString(status))
			.append(")")
			.toString();
	}
}
