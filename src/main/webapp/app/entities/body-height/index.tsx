import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import BodyHeight from './body-height';
import BodyHeightDetail from './body-height-detail';
import BodyHeightUpdate from './body-height-update';
import BodyHeightDeleteDialog from './body-height-delete-dialog';

const BodyHeightRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<BodyHeight />} />
    <Route path="new" element={<BodyHeightUpdate />} />
    <Route path=":id">
      <Route index element={<BodyHeightDetail />} />
      <Route path="edit" element={<BodyHeightUpdate />} />
      <Route path="delete" element={<BodyHeightDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BodyHeightRoutes;
