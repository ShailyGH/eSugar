import dayjs from 'dayjs';
import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface IUserBMR {
  id?: number;
  idVersion?: number | null;
  bmr?: number | null;
  dtCreated?: dayjs.Dayjs | null;
  dtModified?: dayjs.Dayjs | null;
  userProfile?: IUserProfile | null;
}

export const defaultValue: Readonly<IUserBMR> = {};
