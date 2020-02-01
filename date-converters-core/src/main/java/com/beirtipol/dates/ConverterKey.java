/*
 * Copyright (C) 2020  https://github.com/beirtipol
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.beirtipol.dates;

/**
 * Simple POJO for indexing all available conversion pairs in the {@link Converters} {@link org.springframework.beans.factory.config.BeanPostProcessor}
 */
public class ConverterKey {
	private final Class<?>	from;
	private final Class<?>	to;

	public ConverterKey(Class<?> from, Class<?> to) {
		this.from = from;
		this.to = to;
	}

	public Class<?> getFrom() {
		return from;
	}

	public Class<?> getTo() {
		return to;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConverterKey other = (ConverterKey) obj;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (to == null) {
			return other.to == null;
		} else return to.equals(other.to);
	}

	@Override
	public String toString() {
		return "[from=" + from + ", to=" + to + "]";
	}

}