import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserBMR from './user-bmr';
import UserBMRDetail from './user-bmr-detail';
import UserBMRUpdate from './user-bmr-update';
import UserBMRDeleteDialog from './user-bmr-delete-dialog';

const UserBMRRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserBMR />} />
    <Route path="new" element={<UserBMRUpdate />} />
    <Route path=":id">
      <Route index element={<UserBMRDetail />} />
      <Route path="edit" element={<UserBMRUpdate />} />
      <Route path="delete" element={<UserBMRDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserBMRRoutes;
