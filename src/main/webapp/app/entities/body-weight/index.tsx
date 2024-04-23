import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import BodyWeight from './body-weight';
import BodyWeightDetail from './body-weight-detail';
import BodyWeightUpdate from './body-weight-update';
import BodyWeightDeleteDialog from './body-weight-delete-dialog';

const BodyWeightRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<BodyWeight />} />
    <Route path="new" element={<BodyWeightUpdate />} />
    <Route path=":id">
      <Route index element={<BodyWeightDetail />} />
      <Route path="edit" element={<BodyWeightUpdate />} />
      <Route path="delete" element={<BodyWeightDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BodyWeightRoutes;
