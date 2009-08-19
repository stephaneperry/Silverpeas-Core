package com.stratelia.webactiv.calendar.control;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.stratelia.webactiv.calendar.model.HolidayDetail;
import com.stratelia.webactiv.calendar.model.Attendee;
import com.stratelia.webactiv.calendar.model.Category;
import com.stratelia.webactiv.calendar.model.JournalHeader;
import com.stratelia.webactiv.calendar.model.ToDoHeader;

public interface CalendarBmBusinessSkeleton
{
  /**
   * To be able to get user login, CalendarBm need to be connected to admin services
   */
  //public void setUserConnections(AdminUserConnections userConnections);

  /**
   * getDaySchedulablesForUser()
   * for a particular user returns all the events scheduled on
   * a particular day. This includes all kinds of events
   */
  public Collection getDaySchedulablesForUser(String day, String userId, 
    String categoryId, String participation) 
    throws RemoteException;
  
  /**
   * getNextDaySchedulablesForUser()
   * for a particular user returns the next events scheduled.
   * This includes all kinds of events
   */
  public Collection getNextDaySchedulablesForUser(String day, String userId, 
    String categoryId, String participation) 
    throws RemoteException;
  
  /**
   * getPeriodSchedulablesForUser()
   * for a particular user returns all the events scheduled during
   * a particular period. This includes all kinds of events
   */
  public Collection getPeriodSchedulablesForUser(String begin, String end, 
    String userId, String categoryId, String participation) 
    throws RemoteException;
  
  /**
   * countMonthSchedulablesForUser()
   * for a particular user, counts the number of schedules
   * for each day in the month
   */
  public Collection countMonthSchedulablesForUser(String month, String userId, 
    String categoryId, String participation) 
    throws RemoteException;
  
  /**
   * methods for tentative schedules (not yet accepted or declined events)
   */
  public boolean hasTentativeSchedulablesForUser(String userId) 
    throws RemoteException;
  
  public Collection getTentativeSchedulablesForUser(String userId) 
    throws RemoteException;
  
  public Collection getNotCompletedToDosForUser(String userId) 
    throws RemoteException;
  
  public Collection getOrganizerToDos(String organizerId) 
    throws RemoteException;
  
  public Collection getClosedToDos(String organizerId) 
    throws RemoteException;
  

  public Collection getExternalTodos(String spaceId, String componentId, String externalId)
    throws RemoteException;

  /**
   * addJournal()
   * add a journal entry in the database
   */
  public String addJournal(JournalHeader journal)
    throws RemoteException, CalendarException;
  
  /**
   * addToDo()
   * add a todo entry in the database
   */
  public String addToDo(ToDoHeader todo)
    throws RemoteException, CalendarException;
  
  /**
   * updateJournal()
   * update the journal entry, specified by the id, in the database
   */
  public void updateJournal(JournalHeader journal)
    throws RemoteException, CalendarException;
  
  /**
   * updateToDo()
   * update the todo entry, specified by the id, in the database
   */
  /*
  public void updateToDo(ToDoHeader todo) 
    throws RemoteException, CreateException;
  */
  public void updateToDo(ToDoHeader todo) 
    throws RemoteException, CalendarException;
  
  /**
   * removeJournal()
   * remove the journal entry specified by the id
   */
  public void removeJournal(String journalId)
    throws RemoteException;
  
  /**
   * removeToDo()
   * remove the todo entry specified by the id
   */
  public void removeToDo(String id)
    throws RemoteException;
    
  /**
   * removeToDoByInstanceId
   * remove all todo of the specified instance 
   */
  public void removeToDoByInstanceId(String instanceId)
  	throws RemoteException;
  
  /**
   * getJournalHeader()
   * returns the journalHeader represented by the journalId
   */
  public JournalHeader getJournalHeader(String journalId)
    throws RemoteException;

  /**
   * getOutlookJournalHeadersForUser()
   * returns the journalHeaders for user represented by the userId
   */
  public Collection getExternalJournalHeadersForUser(String userId)
    throws RemoteException;

  /**
   * getExternalJournalHeadersForUserAfterDate()
   * returns the journalHeaders for user represented by the userId for which start date after given date
   */
  public Collection getExternalJournalHeadersForUserAfterDate(String userId, Date startDate)
    throws RemoteException;
  
  /**
   * getJournalHeadersForUserAfterDate()
   * returns the journalHeaders for user represented by the userId for which start date after given date
   */
  public Collection getJournalHeadersForUserAfterDate(String userId, Date startDate, int nbReturned)
    throws RemoteException;

  /**
   * getToDoHeader()
   * returns the ToDoHeader represented by the todoId
   */
  public ToDoHeader getToDoHeader(String todoId)
    throws RemoteException;
  
  /**
   * methods for attendees
   */
  public void addJournalAttendee(String journalId, Attendee attendee)
    throws RemoteException;
  
  public void removeJournalAttendee(String journalId, Attendee attendee)
    throws RemoteException;
  
  public Collection getJournalAttendees(String journalId)
    throws RemoteException;
  
  public void setJournalAttendees(String journalId, String[] userIds) 
    throws RemoteException;
  
  public void setJournalParticipationStatus(String journalId, String userId, String participation) 
    throws RemoteException;
 
  public void addToDoAttendee(String todoId, Attendee attendee)
    throws RemoteException;
  
  public void removeToDoAttendee(String todoId, Attendee attendee)
    throws RemoteException;
  
  public Collection getToDoAttendees(String todoId)
    throws RemoteException;
  
  public void setToDoAttendees(String todoId, String[] userIds) 
    throws RemoteException;
  
  /**
   * methods for categories
   */
  public Collection getAllCategories() 
    throws RemoteException;
 
  public Category getCategory(String categoryId) 
    throws RemoteException;
  
  public Collection getJournalCategories(String journalId) 
    throws RemoteException;
  
  public void addJournalCategory(String journalId, String categoryId) 
    throws RemoteException;
  
  public void removeJournalCategory(String journalId, String categoryId) 
    throws RemoteException;
  
  public void setJournalCategories(String journalId, String[] categoryIds) 
    throws RemoteException;

  // methods for reindexation
  public void indexAllTodo() throws RemoteException;

  public void indexAllJournal() throws RemoteException;

	// Gestion des jours non travaill�s
	public boolean isHolidayDate(HolidayDetail date) throws RemoteException;	
	public List getHolidayDates(String userId) throws RemoteException;
	public List getHolidayDates(String userId, Date beginDate, Date endDate) throws RemoteException;
	public void addHolidayDate(HolidayDetail holiday) throws RemoteException;
	public void addHolidayDates(List holidayDates) throws RemoteException;
	public void removeHolidayDate(HolidayDetail holiday) throws RemoteException;
	public void removeHolidayDates(List holidayDates) throws RemoteException;


}