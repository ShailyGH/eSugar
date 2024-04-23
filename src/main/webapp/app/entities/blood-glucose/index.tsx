import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import BloodGlucose from './blood-glucose';
import BloodGlucoseDetail from './blood-glucose-detail';
import BloodGlucoseUpdate from './blood-glucose-update';
import BloodGlucoseDeleteDialog from './blood-glucose-delete-dialog';

const BloodGlucoseRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<BloodGlucose />} />
    <Route path="new" element={<BloodGlucoseUpdate />} />
    <Route path=":id">
      <Route index element={<BloodGlucoseDetail />} />
      <Route path="edit" element={<BloodGlucoseUpdate />} />
      <Route path="delete" element={<BloodGlucoseDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BloodGlucoseRoutes;
