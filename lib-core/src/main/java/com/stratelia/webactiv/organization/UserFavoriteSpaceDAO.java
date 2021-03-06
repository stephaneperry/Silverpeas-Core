/**
 * Copyright (C) 2000 - 2011 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have received a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "http://repository.silverpeas.com/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.stratelia.webactiv.organization;

import java.util.List;

/**
 * UserFavoriteSpace DAO interface
 */
public interface UserFavoriteSpaceDAO {

  /**
   * Retrieve the list of user favorite space
   * @param userId : the user identifier
   * @return the list of User Favorite Space Value Object
   */
  public List<UserFavoriteSpaceVO> getListUserFavoriteSpace(String userId);

  /**
   * Add given User Favorite Space Value Object parameter in Database <br>
   * @param ufsVO a UserFavoriteSpaceVO
   * @return true if action was successful, false else if
   */
  public boolean addUserFavoriteSpace(UserFavoriteSpaceVO ufsVO);

  /**
   * Remove given User Favorite Space Value Object from Database
   * <ul>
   * <li>remove one record if ufsVO.userId and ufsVO.spaceId is not null</li>
   * <li>remove all ufsVO.spaceId if ufsVO.userId is null</li>
   * <li>remove all ufsVO.userId if ufsVO.spaceId is null</li>
   * </ul>
   * @param ufsVO a UserFavoriteSpaceVO
   * @return true if action was successful, false else if
   */
  public boolean removeUserFavoriteSpace(UserFavoriteSpaceVO ufsVO);

}
