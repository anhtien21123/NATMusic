# Design System Specification (v1.0) - NATMusic Implementation

## 1. Introduction
This Design System serves as the **"Single Source of Truth"** for the NATMusic mobile application. It is designed to ensure UI consistency across independent **Dynamic Modules** while optimizing code reusability, theme scalability, and performance. 

By centralizing our design logic, we enable:
- **Consistency:** Uniform look and feel across Home, Search, Player, and Profile modules.
- **Scalability:** Easy adaptation to Dark/Light modes or brand changes.
- **Efficiency:** Developers use pre-built components rather than recreating UI from scratch.

---

## 2. Architecture & Integration Strategy
In a **Dynamic Module** environment, the Design System follows a strict hierarchical dependency to prevent circular references and maintain a clean separation of concerns.

### 2.1. Layering
1.  **`:core:ui` (The Foundation):** 
    - Contains all **Design Tokens** (Color, Typography, Spacing).
    - Contains **Base Components** (Atoms) like Buttons, Inputs, and custom ViewGroups.
    - Export the `AppTheme` provider.
    - **Rule:** This module has ZERO dependencies on feature modules or business logic.
2.  **`:feature:[name]` (The Implementation):** 
    - Contains business logic and feature-specific UI (Molecules/Organisms).
    - **Rule:** It must strictly use components and tokens exported from `:core:ui`. It should never define its own "local" colors or hardcoded margins.

### 2.2. Folder Structure (`:core:ui`)
A logical organization is key to discoverability. We follow a modified Atomic Design structure:

```text
core-ui/
├── theme/
│   ├── Color.kt / .swift      # Functional color definitions (Primary, Surface, Glass)
│   ├── Typography.kt / .swift # Font scales, weights, and Line Heights
│   ├── Shape.kt               # Corner radius definitions (Small, Medium, Large)
│   └── AppTheme.kt            # CompositionLocalProviders & Theme wrapper
├── components/
│   ├── buttons/               # AppButton, AppIconButton, PlayPauseButton
│   ├── inputs/                # AppTextField, AppSearchBox
│   └── layout/                # AppScaffold, AppCard, SectionHeader
├── atoms/
│   ├── GlassBox.kt            # Glassmorphism base component with blur & border
│   ├── Spacing.kt             # Margin/Padding constants (8dp, 16dp, 24dp)
│   └── Shimmer.kt             # Loading placeholders logic
└── icons/                     # Vector assets, SVG wrappers, and Icon Packs
```

---

## 3. Design Tokens (The DNA)

### 3.1. Color System (Dynamic & Functional)
We use a functional naming convention rather than literal color names.

| Token | Hex/Value | Usage |
| :--- | :--- | :--- |
| `background` | `#0A0A0A` | Nền chính (Đen sâu, sang trọng) |
| `surface-glass` | `rgba(255, 255, 255, 0.08)` | Glassmorphism Base |
| `surface-border` | `rgba(255, 255, 255, 0.15)` | Viền (Stroke) của Glass |
| `shimmer-base` | `#1A1A1A` | Màu nền của Skeleton |
| `shimmer-highlight` | `#2D2D2D` | Màu luồng sáng di chuyển |
| `text-primary` | `#FFFFFF` | Tiêu đề |
| `text-secondary` | `#A0A0A0` | Nội dung phụ |
| `primary` | `#BB86FC` | Brand actions, highlight states |

### 3.2. Typography
NATMusic utilizes a hierarchy specialized for readability in music interfaces.

- **Display:** (32sp+) - Hero headers, Artist names in Player.
- **Headline:** (24sp) - Section titles (e.g., "Recently Played").
- **Title:** (18sp) - Album names, Playlist titles.
- **Body:** (14sp/16sp) - Standard text, descriptions.
- **Label:** (12sp) - Metadata (Duration, Release date).

### 3.3. Spacing Grid
To maintain balance, all layouts must align with the **4dp Grid System**.
- `SpaceSmall`: 4dp
- `SpaceMedium`: 8dp
- `SpaceLarge`: 16dp
- `SpaceExtraLarge`: 24dp
- `ScreenPadding`: 16dp (Standard side margin)

---

## 4. Premium Components & Effects

### 4.1. Glassmorphism (The NATMusic Identity)
One of the core visual anchors is the **GlassBox**. It must provide:
- **Backdrop Blur:** Minimum 15px.
- **Translucent Fill:** Low opacity white/black.
- **Subtle Border:** 1px width with gradient to simulate "glass edge".

```kotlin
@Composable
fun AppGlassBox(
    modifier: Modifier = Modifier,
    blur: Dp = 20.dp,
    content: @Composable () -> Unit
) {
    // Implementation uses native blur modifiers or legacy fallbacks
}
```

### 4.2. Image Shadows & Elevations
Instead of standard Android Elevation, NATMusic uses **Colored Shadows** for albums to create a "glowing" effect that matches the primary color of the album art.

---

## 5. Development Workflow
1.  **Checking for Components:** Before building a new UI, check `:core:ui:components` to see if it already exists.
2.  **Modifying the System:** If a design token needs to change, it must be changed in `:core:ui`. Features will inherit the change automatically.
3.  **Naming Convention:** All components in this module should be prefixed with `App` or `NAT` (e.g., `AppButton`, `NATScaffold`).

---

## 6. Accessibility & Compliance
- **Contrast:** Ensure all text-on-surface combinations meet WCAG AA standards (4.5:1 ratio).
- **Touch Targets:** Minimum 48x48dp for all interactive elements.
- **Screen Readers:** Provide `contentDescription` for all functional icons.