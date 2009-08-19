package com.silverpeas.workflow.api.task;

import com.silverpeas.form.DataRecord;
import com.silverpeas.workflow.api.event.QuestionEvent;
import com.silverpeas.workflow.api.event.ResponseEvent;
import com.silverpeas.workflow.api.event.TaskDoneEvent;
import com.silverpeas.workflow.api.instance.HistoryStep;
import com.silverpeas.workflow.api.instance.ProcessInstance;
import com.silverpeas.workflow.api.instance.Question;
import com.silverpeas.workflow.api.model.ProcessModel;
import com.silverpeas.workflow.api.model.State;
import com.silverpeas.workflow.api.user.User;

/**
 * A task object is an activity description
 * built by the workflow engine
 * and sent via the taskManager to an external system 
 * which will notify the end user and manage the task realisation. 
 *
 * Task objects will be created by the workflow engine when a new task
 * is assigned to a user.
 * Task objects will be created too for the ProcessManager GUI which
 * will be used by the user to do the assigned activity.
 */
public interface Task
{
    /**
	 * Returns the actor.
	 */
    public User getUser();

    /**
	 * Returns the name of the role which gived the responsability
	 * of this task to the user.
	 */
    public String getUserRoleName();

	/**
	 * Returns the process model (peas).
	 *
	 * The id of this workflow internal information
	 * must be stored by the external system
	 * to be sent to the workflow engine when the activity is done.
	 */
    public ProcessModel getProcessModel();

	/**
	 * Returns the process instance.
	 *
	 * The id of this workflow internal information
	 * must be stored by the external system
	 * to be sent to the workflow engine when the activity is done.
	 */
    public ProcessInstance getProcessInstance();

	/**
	 * Returns the state/activity to be resolved by the user.
	 *
	 * The name of this workflow internal information
	 * must be stored by the external system
	 * to be sent to the workflow engine when the activity is done.
	 */
	public State getState();

    /**
     * Returns the history steps that user can discussed (ask a question to the actor of that step).
     */
    public HistoryStep[] getBackSteps();

    /**
     * Returns the question that must be answered
     */
    public Question[] getPendingQuestions();

    /**
     * Returns the (non onsolete) questions that have been answered
     */
    public Question[] getRelevantQuestions();

	/**
     * Returns the question that have been asked and are waiting for a response
     */
    public Question[] getSentQuestions();

	/**
	 * Returns the action names list from which the user must choose
	 * to resolve the activity.
	 */
	public String[] getActionNames();

    /**
	 * When this Task is done, builds a TaskDoneEvent
	 * giving the choosed action name and the filled form.
	 */
	public TaskDoneEvent buildTaskDoneEvent(String actionName,
	                                        DataRecord  data
											);

    /**
	 * When this Question is asked for a task, builds a QuestionEvent
	 * giving the choosed step that must give the answer.
	 */
	public QuestionEvent buildQuestionEvent(String stepId,
	                                        DataRecord  data
											);

    /**
	 * When this Response is answer for a task, builds a ResponseEvent
	 * giving the question id that must give the answer.
	 */
	public ResponseEvent buildResponseEvent(String questionId,
	                                        DataRecord  data
											);
}