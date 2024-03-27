package ch.uzh.ifi.hase.soprafs24.constant;

public enum TaskStatus {
  DONE, IN_PROGRESS, WAITING, ;
/*   Posted (waiting to select a candidate),
  Waiting (a time to accomplish the task is set but has not yet started)
  In progress (the task is being done in this moment)
  Done (the task has been done but no reviews has been left)
  Deleted (the advertiser has deleted the ad)
  Successful Reviewed (both parties are satisfied)
  Controversy (one party has raised a complain)
  Deadlock (one party does not review after the review's deadline has passed) */
}
