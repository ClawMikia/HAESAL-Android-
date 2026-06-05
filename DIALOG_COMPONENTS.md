# Dark Themed UI Components

This project includes a set of dark-themed UI components to ensure consistency across the app.

## Components

### 1. Confirmation Dialog

Used for user confirmations like delete actions.

**Usage:**
```kotlin
DialogHelper.showConfirmDialog(
    context = this,
    title = "Clear History",
    message = "Delete all battery history records?",
    onPositive = { 
        viewModel.clearHistory()
    }
)
```

### 2. Message Dialog

Used for displaying messages to the user.

**Usage:**
```kotlin
DialogHelper.showMessageDialog(
    context = this,
    title = "Success",
    message = "Operation completed successfully",
    onDismiss = {
        // Optional callback when dialog is dismissed
    }
)
```

### 3. Toast Helper

Used for showing brief messages to the user.

**Usage:**
```kotlin
ToastHelper.showToast(context, "Operation completed successfully")
```

### 4. Snackbar Helper

Used for showing messages with optional actions.

**Usage:**
```kotlin
// Simple snackbar
SnackbarHelper.showSnackbar(view, "Operation completed")

// Snackbar with action
SnackbarHelper.showSnackbarWithAction(
    view,
    "Network connection lost",
    "Retry",
    View.OnClickListener { /* retry action */ }
)
```

### 5. Loading Dialog

Used for showing loading states.

**Usage:**
```kotlin
// Show loading dialog
LoadingDialog.show(context, "Processing...")

// Hide loading dialog
LoadingDialog.hide()
```

### 6. Bottom Sheet Helper

Used for showing options in a bottom sheet.

**Usage:**
```kotlin
BottomSheetHelper.showOptionsBottomSheet(
    context = this,
    title = "Select Action",
    options = listOf("Option 1", "Option 2", "Option 3"),
    onOptionSelected = { position ->
        // Handle option selection
    }
)
```

## Styling

All components follow the app's dark theme with:
- Background colors from `colors.xml`
- Text colors from `colors.xml`
- Monospace font for a tech/cyberpunk feel
- Glowing accent colors for buttons and highlights

## Integration

These components are designed to be easily integrated throughout the app while maintaining a consistent dark theme.