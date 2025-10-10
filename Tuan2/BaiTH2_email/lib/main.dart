import 'package:flutter/material.dart';
import 'src/app/app.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(const MyApp());
}

/// Backwards-compatible wrapper so tests or other code that expect
/// `MyApp` still work. It simply forwards to the real `App` widget.
class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) => const App();
}
