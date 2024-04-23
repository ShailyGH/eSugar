import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface IBodyHeight {
  id?: number;
  height?: number | null;
  userProfile?: IUserProfile | null;
}

export const defaultValue: Readonly<IBodyHeight> = {};
