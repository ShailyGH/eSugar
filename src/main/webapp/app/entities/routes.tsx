import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserAccount from './user-account';
import UserProfile from './user-profile';
import CharacteristicData from './characteristic-data';
import ProfileDashboardConfig from './profile-dashboard-config';
import BloodGlucose from './blood-glucose';
import BodyVitalsLog from './body-vitals-log';
import BodyWeight from './body-weight';
import BodyHeight from './body-height';
import DataSource from './data-source';
import Activity from './activity';
import UserBMR from './user-bmr';
import ActivityLog from './activity-log';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="user-account/*" element={<UserAccount />} />
        <Route path="user-profile/*" element={<UserProfile />} />
        <Route path="characteristic-data/*" element={<CharacteristicData />} />
        <Route path="profile-dashboard-config/*" element={<ProfileDashboardConfig />} />
        <Route path="blood-glucose/*" element={<BloodGlucose />} />
        <Route path="body-vitals-log/*" element={<BodyVitalsLog />} />
        <Route path="body-weight/*" element={<BodyWeight />} />
        <Route path="body-height/*" element={<BodyHeight />} />
        <Route path="data-source/*" element={<DataSource />} />
        <Route path="activity/*" element={<Activity />} />
        <Route path="user-bmr/*" element={<UserBMR />} />
        <Route path="activity-log/*" element={<ActivityLog />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
