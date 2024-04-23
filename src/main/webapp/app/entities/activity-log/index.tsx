import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ActivityLog from './activity-log';
import ActivityLogDetail from './activity-log-detail';
import ActivityLogUpdate from './activity-log-update';
import ActivityLogDeleteDialog from './activity-log-delete-dialog';

const ActivityLogRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ActivityLog />} />
    <Route path="new" element={<ActivityLogUpdate />} />
    <Route path=":id">
      <Route index element={<ActivityLogDetail />} />
      <Route path="edit" element={<ActivityLogUpdate />} />
      <Route path="delete" element={<ActivityLogDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ActivityLogRoutes;
