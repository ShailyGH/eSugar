import { IActivityLog } from 'app/shared/model/activity-log.model';

export interface IActivity {
  id?: number;
  activityName?: string | null;
  activityMultiplier?: number | null;
  activityLog?: IActivityLog | null;
}

export const defaultValue: Readonly<IActivity> = {};
