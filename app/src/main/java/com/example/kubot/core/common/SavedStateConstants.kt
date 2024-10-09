package com.example.kubot.core.common

object SavedStateConstants {

    // Login / Register screens
    const val SAVED_STATE_username                               = "username"
    const val SAVED_STATE_email                                  = "email"
    const val SAVED_STATE_password                               = "password"
    const val SAVED_STATE_confirmPassword                        = "confirmPassword"
    const val SAVED_STATE_isInvalidEmail                         = "isInvalidEmail"
    const val SAVED_STATE_isInvalidEmailMessageVisible           = "isShowInvalidEmailMessage"
    const val SAVED_STATE_isInvalidPassword                      = "isInvalidPassword"
    const val SAVED_STATE_isInvalidPasswordMessageVisible        = "isShowInvalidPasswordMessage"
    const val SAVED_STATE_isInvalidConfirmPassword               = "isInvalidConfirmPassword"
    const val SAVED_STATE_isInvalidConfirmPasswordMessageVisible = "isShowInvalidConfirmPasswordMessage"
    const val SAVED_STATE_isPasswordsMatch                       = "isPasswordsMatch"
    const val SAVED_STATE_statusMessage                          = "statusMessage"
    const val SAVED_STATE_errorMessage                           = "errorMessage"

    // App-wide
    const val SAVED_STATE_authInfo                               = "authInfo"
    const val SAVED_STATE_isLoaded                               = "isLoaded"
    const val SAVED_STATE_isEditable                             = "isEditable"
    const val SAVED_STATE_editMode                               = "editMode"
    const val SAVED_STATE_startDate                              = "startDate"

    // Editors for process death
    const val SAVED_STATE_savedEditedAgendaItem                  = "savedAgendaItem"

    // Agenda screen
    const val SAVED_STATE_selectedDayIndex                       = "selectedDayIndex"
    const val SAVED_STATE_selectedDate                           = "selectedDate"

    // Event screen
    const val SAVED_STATE_addAttendeeDialogErrorMessage          = "addAttendeeDialogErrorMessage"
    const val SAVED_STATE_isAttendeeEmailValid                   = "isAttendeeEmailValid"
    const val SAVED_STATE_initialEventId                         = "initialEventId"

    // Task screen
    const val SAVED_STATE_initialTaskId                          = "initialTaskId"

    // Reminder screen
    const val SAVED_STATE_initialReminderId                      = "initialReminderId"
}