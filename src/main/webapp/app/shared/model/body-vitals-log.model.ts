import dayjs from 'dayjs';
import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface IBodyVitalsLog {
  id?: number;
  dtCreated?: dayjs.Dayjs | null;
  userProfile?: IUserProfile | null;
}

export const defaultValue: Readonly<IBodyVitalsLog> = {};
