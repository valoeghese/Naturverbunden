/*
 * Naturverbunden
 * Copyright (C) 2021 Valoeghese
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package valoeghese.naturverbunden.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ListArray<T> implements Iterable<List<T>> {
	public ListArray(int size) {
		this.arr = new List[5];
	}

	private final List[] arr;

	public List<T> get(int index) {
		return this.arr[index];
	}

	public List<T> getOrCreate(int index) {
		return NVBContainerUtils.computeIfAbsent(this.arr, index, $ -> new ArrayList<>());
	}

	public <E> ListArray<E> map(Function<T, E> mappingFunction) {
		ListArray<E> result = new ListArray<>(this.arr.length);

		for (int i = 0; i < 5; ++i) {
			List<T> list = this.arr[i];

			if (list != null) {
				List<E> next = new ArrayList<>();

				for (T t : list) {
					next.add(mappingFunction.apply(t));
				}

				result.arr[i] = next;
			}
		}

		return result;
	}

	@Override
	public Iterator<List<T>> iterator() {
		return new ArrayIterator();
	}

	class ArrayIterator implements Iterator<List<T>> {
		private int index = 0;

		@Override
		public boolean hasNext() {
			return this.index < arr.length;
		}

		@Override
		public List<T> next() {
			return arr[this.index++];
		}
	}
}
