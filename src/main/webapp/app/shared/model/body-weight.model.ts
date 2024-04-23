import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface IBodyWeight {
  id?: number;
  weight?: number | null;
  userProfile?: IUserProfile | null;
}

export const defaultValue: Readonly<IBodyWeight> = {};
