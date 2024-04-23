import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import BodyVitalsLog from './body-vitals-log';
import BodyVitalsLogDetail from './body-vitals-log-detail';
import BodyVitalsLogUpdate from './body-vitals-log-update';
import BodyVitalsLogDeleteDialog from './body-vitals-log-delete-dialog';

const BodyVitalsLogRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<BodyVitalsLog />} />
    <Route path="new" element={<BodyVitalsLogUpdate />} />
    <Route path=":id">
      <Route index element={<BodyVitalsLogDetail />} />
      <Route path="edit" element={<BodyVitalsLogUpdate />} />
      <Route path="delete" element={<BodyVitalsLogDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BodyVitalsLogRoutes;
