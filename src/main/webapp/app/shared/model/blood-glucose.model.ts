import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface IBloodGlucose {
  id?: number;
  measurement?: number | null;
  measurementContent?: string | null;
  measurementType?: string | null;
  userProfile?: IUserProfile | null;
}

export const defaultValue: Readonly<IBloodGlucose> = {};
