import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DataSource from './data-source';
import DataSourceDetail from './data-source-detail';
import DataSourceUpdate from './data-source-update';
import DataSourceDeleteDialog from './data-source-delete-dialog';

const DataSourceRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DataSource />} />
    <Route path="new" element={<DataSourceUpdate />} />
    <Route path=":id">
      <Route index element={<DataSourceDetail />} />
      <Route path="edit" element={<DataSourceUpdate />} />
      <Route path="delete" element={<DataSourceDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DataSourceRoutes;
