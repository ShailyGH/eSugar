import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/user-account">
        User Account
      </MenuItem>
      <MenuItem icon="asterisk" to="/user-profile">
        User Profile
      </MenuItem>
      <MenuItem icon="asterisk" to="/characteristic-data">
        Characteristic Data
      </MenuItem>
      <MenuItem icon="asterisk" to="/profile-dashboard-config">
        Profile Dashboard Config
      </MenuItem>
      <MenuItem icon="asterisk" to="/blood-glucose">
        Blood Glucose
      </MenuItem>
      <MenuItem icon="asterisk" to="/body-vitals-log">
        Body Vitals Log
      </MenuItem>
      <MenuItem icon="asterisk" to="/body-weight">
        Body Weight
      </MenuItem>
      <MenuItem icon="asterisk" to="/body-height">
        Body Height
      </MenuItem>
      <MenuItem icon="asterisk" to="/data-source">
        Data Source
      </MenuItem>
      <MenuItem icon="asterisk" to="/activity">
        Activity
      </MenuItem>
      <MenuItem icon="asterisk" to="/user-bmr">
        User BMR
      </MenuItem>
      <MenuItem icon="asterisk" to="/activity-log">
        Activity Log
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
