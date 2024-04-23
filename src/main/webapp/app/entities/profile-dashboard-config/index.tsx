import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ProfileDashboardConfig from './profile-dashboard-config';
import ProfileDashboardConfigDetail from './profile-dashboard-config-detail';
import ProfileDashboardConfigUpdate from './profile-dashboard-config-update';
import ProfileDashboardConfigDeleteDialog from './profile-dashboard-config-delete-dialog';

const ProfileDashboardConfigRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ProfileDashboardConfig />} />
    <Route path="new" element={<ProfileDashboardConfigUpdate />} />
    <Route path=":id">
      <Route index element={<ProfileDashboardConfigDetail />} />
      <Route path="edit" element={<ProfileDashboardConfigUpdate />} />
      <Route path="delete" element={<ProfileDashboardConfigDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ProfileDashboardConfigRoutes;
