import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface IProfileDashboardConfig {
  id?: number;
  isBloodGlucoseShown?: string | null;
  isBloodPressureShown?: string | null;
  isBodyCompositionShown?: string | null;
  isBloodCholesterolShown?: string | null;
  isBodyHeightShown?: string | null;
  isBodyWeightShown?: string | null;
  isCaloriesBurntShown?: string | null;
  userProfile?: IUserProfile | null;
}

export const defaultValue: Readonly<IProfileDashboardConfig> = {};
