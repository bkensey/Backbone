/*
 * Copyright (C) 2013 BrandroidTools
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

public class BookmarkDeleteEvent extends BusEvent {
	public final String path;

	public BookmarkDeleteEvent(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return new StringBuilder("(")
			.append("bus event: delete bookmark ")
			.append(path)
			.append(")")
			.toString();
	}
}
