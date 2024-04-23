import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CharacteristicData from './characteristic-data';
import CharacteristicDataDetail from './characteristic-data-detail';
import CharacteristicDataUpdate from './characteristic-data-update';
import CharacteristicDataDeleteDialog from './characteristic-data-delete-dialog';

const CharacteristicDataRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CharacteristicData />} />
    <Route path="new" element={<CharacteristicDataUpdate />} />
    <Route path=":id">
      <Route index element={<CharacteristicDataDetail />} />
      <Route path="edit" element={<CharacteristicDataUpdate />} />
      <Route path="delete" element={<CharacteristicDataDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CharacteristicDataRoutes;
