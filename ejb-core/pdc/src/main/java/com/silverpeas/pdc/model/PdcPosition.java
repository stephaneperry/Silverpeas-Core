/*
 * Copyright (C) 2000 - 2011 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection withWriter Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have recieved a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "http://www.silverpeas.org/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.silverpeas.pdc.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * A position of a content on some axis of the classification plan (named PdC). The positions of 
 * a given content define its classification on the PdC.
 * 
 * A position on the PdC's axis represents an atomic semantic information about its content.
 * As such, it can be made up of one or more values of axis. As a PdC axis is defined by an hierarchic
 * tree of terms, each of them being a value in the concept represented by the axis, a value in a
 * position is defined by its path in the tree from the root; the root being one of the base
 * value of the axis.
 * 
 * For example, for a position on the axis representing the concept of geography, a possible value
 * can be "France / Rhônes-Alpes / Isère / Grenoble" where Grenoble is the last term
 * of the axis valuation.
 */
@Entity
public class PdcPosition implements Serializable {
  private static final long serialVersionUID = 665144316569539208L;
  
  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  private Long id;
  
  @OneToMany(cascade={CascadeType.ALL}, orphanRemoval=true)
  private Set<PdcAxisValue> axisValues = new HashSet<PdcAxisValue>();

  public String getId() {
    return id.toString();
  }

  /**
   * Gets the values of this position on the axis of the PdC.
   * You can add or remove any values from the returned set.
   * @return a set of PdC axis values.
   */
  public Set<PdcAxisValue> getValues() {
    return axisValues;
  }

}