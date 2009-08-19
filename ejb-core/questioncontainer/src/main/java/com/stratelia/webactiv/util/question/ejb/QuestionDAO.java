// TODO : reporter dans CVS (done)
package com.stratelia.webactiv.util.question.ejb;

import java.sql.*;
import java.util.*;
import com.stratelia.webactiv.util.DBUtil;
import com.stratelia.webactiv.util.exception.*;
import com.stratelia.webactiv.util.question.model.*;
import com.stratelia.silverpeas.silvertrace.*;

/*
 * CVS Informations
 * 
 * $Id: QuestionDAO.java,v 1.4 2008/06/26 05:08:34 neysseri Exp $
 * 
 * $Log: QuestionDAO.java,v $
 * Revision 1.4  2008/06/26 05:08:34  neysseri
 * no message
 *
 * Revision 1.3.6.1  2008/06/06 13:05:07  sfariello
 * Ajout zone "style" pour le type de r�ponse
 *
 * Revision 1.3  2003/11/24 10:38:09  cbonin
 * no message
 *
 * Revision 1.2  2002/12/17 10:34:00  scotte
 * Correction erreur : Les questions sont TJS dans le d�sordre
 *
 * Revision 1.1.1.1  2002/08/06 14:47:53  nchaix
 * no message
 *
 * Revision 1.14  2002/01/04 13:53:47  neysseri
 * no message
 *
 * Revision 1.13  2001/12/20 15:46:04  neysseri
 * Stabilisation Lot 2 :
 * Silvertrace et exceptions + javadoc
 *
 */

/**
 * This class is made to access database only (table SB_Question_Question)
 * 
 * @author neysseri
 */
public class QuestionDAO
{

	public static final String QUESTIONCOLUMNNAMES =
		"questionId, qcId, questionLabel, questionDescription, questionClue, questionImage, questionIsQCM, questionType, questionIsOpen, questionCluePenalty, questionMaxTime, questionDisplayOrder, questionNbPointsMin, questionNbPointsMax, instanceId, style";

	/**
	 * Build a Question object with data from the resultset
	 *
	 * @param rs			the Resultset which contains data
	 * @param questionPK	the context
	 *
	 * @return	a Question
	 *
	 * @throws SQLException
	 *
	 */
	private static Question getQuestionFromResultSet(ResultSet rs, QuestionPK questionPK) throws SQLException
	{
		String id = rs.getString(1);
		String fatherId = rs.getString(2);
		String label = rs.getString(3);
		String description = rs.getString(4);
		String clue = rs.getString(5);
		String image = rs.getString(6);
		boolean isQCM = false;

		if (rs.getInt(7) == 1)
		{
			isQCM = true;
		}
		int type = rs.getInt(8);
		boolean isOpen = false;

		if (rs.getInt(9) == 1)
		{
			isOpen = true;
		}
		int cluePenalty = rs.getInt(10);
		int maxTime = rs.getInt(11);
		int displayOrder = rs.getInt(12);
		int nbPointsMin = rs.getInt(13);
		int nbPointsMax = rs.getInt(14);
		//String instanceId = rs.getString(15);	// pas utilis� mais dans la liste des r�cup�rations
		String style = rs.getString(16);

		Question result = new Question(	new QuestionPK(id, questionPK),	fatherId, label, description, clue,
				image, isQCM, type, isOpen, cluePenalty, maxTime, displayOrder, nbPointsMin, nbPointsMax, style);

		return result;
	}

	/**
	 * Return a question
	 *
	 * @param con			the Connection to dataBase
	 * @param questionPK	the question id
	 *
	 * @return a Question
	 *
	 * @throws SQLException
	 *
	 */
	public static Question getQuestion(Connection con, QuestionPK questionPK) throws SQLException
	{
		SilverTrace.info(
			"question",
			"QuestionDAO.getQuestion()",
			"root.MSG_GEN_ENTER_METHOD",
			"questionPK = " + questionPK);
		ResultSet rs = null;
		Question question = null;

		String selectStatement =
			"select " + QUESTIONCOLUMNNAMES + " from " + questionPK.getTableName() + " where questionId = ?";

		PreparedStatement prepStmt = null;

		try
		{
			prepStmt = con.prepareStatement(selectStatement);
			prepStmt.setInt(1, new Integer(questionPK.getId()).intValue());
			rs = prepStmt.executeQuery();
			if (rs.next())
			{
				question = getQuestionFromResultSet(rs, questionPK);
			}
		}
		finally
		{
			DBUtil.close(rs, prepStmt);
		}

		return question;
	}

	/**
	 * Return the questions linked to a given father
	 *
	 * @param con			the Connection to dataBase
	 * @param questionPK	the question id
	 * @param fatherId		the father id
	 *
	 * @return a Collection of Question
	 *
	 * @throws SQLException
	 *
	 */
	public static Collection getQuestionsByFatherPK(Connection con, QuestionPK questionPK, String fatherId)
		throws SQLException
	{
		SilverTrace.info(
			"question",
			"QuestionDAO.getQuestionsByFatherPK()",
			"root.MSG_GEN_ENTER_METHOD",
			"questionPK = " + questionPK + ", fatherId = " + fatherId);

		ArrayList result = new ArrayList();
		ResultSet rs = null;
		Question question = null;

		String selectStatement =
			"select "
				+ QUESTIONCOLUMNNAMES
				+ " from "
				+ questionPK.getTableName()
				+ " where qcId = ? and instanceId = ? ORDER BY questionDisplayOrder";

		PreparedStatement prepStmt = null;

		try
		{
			prepStmt = con.prepareStatement(selectStatement);
			prepStmt.setInt(1, new Integer(fatherId).intValue());
			prepStmt.setString(2, questionPK.getComponentName());
			rs = prepStmt.executeQuery();

			while (rs.next())
			{
				question = getQuestionFromResultSet(rs, questionPK);
				result.add(question);
			}
		}
		finally
		{
			DBUtil.close(rs, prepStmt);
		}

		return result;
	}

	/**
	 * Create a new question
	 *
	 * @param con			the Connection to dataBase
	 * @param question		the Question to create
	 *
	 * @return	the QuestionPK of the new question
	 *
	 * @throws SQLException
	 *
	 */
	public static QuestionPK createQuestion(Connection con, Question question) throws SQLException
	{
		SilverTrace.info(
			"question",
			"QuestionDAO.createQuestion()",
			"root.MSG_GEN_ENTER_METHOD",
			"question = " + question);
		int newId = 0;

		String insertStatement =
			"insert into "
				+ (question.getPK()).getTableName()
				+ " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

		try
		{
			/* Recherche de la nouvelle PK de la table */
			newId = DBUtil.getNextId(question.getPK().getTableName(), new String("questionId"));
		}
		catch (Exception e)
		{
			throw new QuestionRuntimeException(
				"QuestionDAO.createQuestion()",
				SilverpeasRuntimeException.ERROR,
				"root.EX_GET_NEXTID_FAILED",
				e);
		}

		QuestionPK questionPK = question.getPK();

		questionPK.setId(new Integer(newId).toString());

		PreparedStatement prepStmt = null;

		try
		{
			prepStmt = con.prepareStatement(insertStatement);
			prepStmt.setInt(1, newId);
			prepStmt.setInt(2, new Integer(question.getFatherId()).intValue());
			prepStmt.setString(3, question.getLabel());
			prepStmt.setString(4, question.getDescription());
			prepStmt.setString(5, question.getClue());
			prepStmt.setString(6, question.getImage());
			if (question.isQCM())
			{
				prepStmt.setInt(7, 1);
			}
			else
			{
				prepStmt.setInt(7, 0);
			}
			prepStmt.setInt(8, question.getType());
			if (question.isOpen())
			{
				prepStmt.setInt(9, 1);
			}
			else
			{
				prepStmt.setInt(9, 0);
			}
			prepStmt.setInt(10, question.getCluePenalty());
			prepStmt.setInt(11, question.getMaxTime());
			prepStmt.setInt(12, question.getDisplayOrder());
			prepStmt.setInt(13, question.getNbPointsMin());
			prepStmt.setInt(14, question.getNbPointsMax());
			prepStmt.setString(15, question.getPK().getComponentName());
			prepStmt.setString(16, question.getStyle());

			prepStmt.executeUpdate();
		}
		finally
		{
			DBUtil.close(prepStmt);
		}

		return questionPK;
	}

	/**
	 * Update a question
	 *
	 * @param con			the Connection to dataBase
	 * @param question		the Question to update
	 *
	 * @throws SQLException
	 *
	 */
	public static void updateQuestion(Connection con, Question question) throws SQLException
	{
		SilverTrace.info(
			"question",
			"QuestionDAO.updateQuestion()",
			"root.MSG_GEN_ENTER_METHOD",
			"question = " + question);

		String insertStatement =
			"update "
				+ question.getPK().getTableName()
				+ " set questionLabel = ?, questionDescription = ?, questionClue = ?,"
				+ " questionImage = ?, questionIsQCM = ?, questionType = ?, questionIsOpen = ?,"
				+ " questionCluePenalty = ?, questionMaxTime = ?, questionDisplayOrder = ?,"
				+ " questionNbPointsMin = ?, questionNbPointsMax = ?, instanceId = ?, style = ? "
				+ " where questionId = ?";

		PreparedStatement prepStmt = null;

		try
		{
			prepStmt = con.prepareStatement(insertStatement);
			prepStmt.setString(1, question.getLabel());
			prepStmt.setString(2, question.getDescription());
			prepStmt.setString(3, question.getClue());
			prepStmt.setString(4, question.getImage());
			if (question.isQCM())
			{
				prepStmt.setInt(5, 1);
			}
			else
			{
				prepStmt.setInt(5, 0);
			}
			prepStmt.setInt(6, question.getType());
			if (question.isOpen())
			{
				prepStmt.setInt(7, 1);
			}
			else
			{
				prepStmt.setInt(7, 0);
			}
			prepStmt.setInt(8, question.getCluePenalty());
			prepStmt.setInt(9, question.getMaxTime());
			prepStmt.setInt(10, question.getDisplayOrder());
			prepStmt.setInt(11, question.getNbPointsMin());
			prepStmt.setInt(12, question.getNbPointsMax());
			prepStmt.setString(13, question.getPK().getComponentName());
			prepStmt.setString(14, question.getStyle());
			prepStmt.setInt(15, new Integer(question.getPK().getId()).intValue());
			

			prepStmt.executeUpdate();
		}
		finally
		{
			DBUtil.close(prepStmt);
		}
	}

	/**
	 * Delete a question
	 *
	 * @param con			the Connection to dataBase
	 * @param questionPK	the question id
	 *
	 * @throws SQLException
	 *
	 */
	public static void deleteQuestion(Connection con, QuestionPK questionPK) throws SQLException
	{
		SilverTrace.info(
			"question",
			"QuestionDAO.deleteQuestion()",
			"root.MSG_GEN_ENTER_METHOD",
			"questionPK = " + questionPK);

		String deleteStatement = "delete from " + questionPK.getTableName() + " where questionId = ? ";

		PreparedStatement prepStmt = null;

		try
		{
			prepStmt = con.prepareStatement(deleteStatement);
			prepStmt.setInt(1, new Integer(questionPK.getId()).intValue());
			prepStmt.executeUpdate();
		}
		finally
		{
			DBUtil.close(prepStmt);
		}
	}

	/**
	 * Delete all questions linked to a given father
	 *
	 * @param con			the Connection to dataBase
	 * @param questionPK	to know the context
	 * @param fatherId		the father id
	 *
	 * @throws SQLException
	 *
	 */
	public static void deleteQuestionsByFatherPK(Connection con, QuestionPK questionPK, String fatherId)
		throws SQLException
	{
		SilverTrace.info(
			"question",
			"QuestionDAO.deleteQuestionsByFatherPK()",
			"root.MSG_GEN_ENTER_METHOD",
			"questionPK = " + questionPK + ", fatherId = " + fatherId);

		String deleteStatement = "delete from " + questionPK.getTableName() + " where qcId = ? and instanceId = ?";

		PreparedStatement prepStmt = null;

		try
		{
			prepStmt = con.prepareStatement(deleteStatement);
			prepStmt.setInt(1, new Integer(fatherId).intValue());
			prepStmt.setString(2, questionPK.getComponentName());
			prepStmt.executeUpdate();
		}
		finally
		{
			DBUtil.close(prepStmt);
		}
	}

}