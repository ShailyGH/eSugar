import dayjs from 'dayjs';
import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface ICharacteristicData {
  id?: number;
  dateOfBirth?: dayjs.Dayjs | null;
  gender?: string | null;
  bloodType?: string | null;
  userProfile?: IUserProfile | null;
}

export const defaultValue: Readonly<ICharacteristicData> = {};
