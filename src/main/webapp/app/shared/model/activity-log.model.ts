import dayjs from 'dayjs';
import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface IActivityLog {
  id?: number;
  startDateTime?: dayjs.Dayjs | null;
  endDateTime?: dayjs.Dayjs | null;
  distanceCovered?: number | null;
  stepsCount?: number | null;
  caloriesBurnt?: number | null;
  userProfile?: IUserProfile | null;
}

export const defaultValue: Readonly<IActivityLog> = {};
