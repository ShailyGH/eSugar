import { IUserAccount } from 'app/shared/model/user-account.model';

export interface IUserProfile {
  id?: number;
  userProfileName?: string | null;
  email?: string | null;
  isReportSharingEnabled?: string | null;
  isActive?: string | null;
  userAccount?: IUserAccount | null;
}

export const defaultValue: Readonly<IUserProfile> = {};
